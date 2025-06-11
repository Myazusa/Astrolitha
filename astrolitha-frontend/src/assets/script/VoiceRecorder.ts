import {ref, watch} from 'vue'
import axios from 'axios'
import {useModelStore, useRecorderStore} from '@/store/Live2DStudioStore'
import { ElMessage } from 'element-plus'
import {useApiStore} from "@/store/ApiStore";
import audioBufferToWav from 'audiobuffer-to-wav';
import {sendQuestion} from "@/assets/script/SendQuestion";
import {voiceGenerator} from "@/assets/script/VoiceGenerator";
import {extractAndRemoveEPlaceholders, removeEnglishCharacters} from "@/assets/script/Utils";
import {mouthControl} from "@/assets/script/MouthControl";

const audioContext = new AudioContext()
const returnAudioContext = new AudioContext()
const threshold = 0.02
const silenceTimeout = 2000

export function VoiceRecorder() {
    const mediaStream = ref<MediaStream | null>(null)
    const workletNode = ref<AudioWorkletNode | null>(null)
    const source = ref<MediaStreamAudioSourceNode | null>(null)
    const isRecording = ref(false)
    const chunks = ref<Float32Array[]>([])
    const silenceTimer = ref<number | null>(null)

    const recorderStore = useRecorderStore()
    const apiStore = useApiStore()

    // 开始录音
    async function startRecording() {
        try {
            mediaStream.value = await navigator.mediaDevices.getUserMedia({ audio: true })

            source.value = audioContext.createMediaStreamSource(mediaStream.value)

            await audioContext.audioWorklet.addModule('/scripts/worklets/recorder-worklet-processor.js')
            workletNode.value = new AudioWorkletNode(audioContext, 'recorder-worklet-processor')

            source.value.connect(workletNode.value)
            workletNode.value.connect(audioContext.destination)

            workletNode.value.port.onmessage = (event) => {
                if (!recorderStore.isRecording || recorderStore.waitingResponse) return

                const input = event.data as Float32Array
                const volume = Math.sqrt(input.reduce((sum, s) => sum + s * s, 0) / input.length)

                if (volume > threshold) {
                    isRecording.value = true
                    chunks.value.push(new Float32Array(input))
                    if (silenceTimer.value) {
                        clearTimeout(silenceTimer.value)
                        silenceTimer.value = null
                    }
                } else if (isRecording.value) {
                    if (!silenceTimer.value) {
                        silenceTimer.value = window.setTimeout(() => {
                            stopAndSendRecording()
                        }, silenceTimeout)
                    }
                }
            }
        } catch (err) {
            ElMessage.error('獲取麥克風失敗：'+err)
            recorderStore.isRecording = false
        }
    }

    // 裁剪出高于阈值的音频
    function flattenChunks(): Float32Array {
        const totalLength = chunks.value.reduce((sum, chunk) => sum + chunk.length, 0)
        const result = new Float32Array(totalLength)
        let offset = 0
        for (const chunk of chunks.value) {
            result.set(chunk, offset)
            offset += chunk.length
        }
        return result
    }

    // 停止录音并发送录音
    function stopAndSendRecording() {
        isRecording.value = false
        silenceTimer.value && clearTimeout(silenceTimer.value)
        silenceTimer.value = null

        if (chunks.value.length === 0) return

        recorderStore.waitingResponse = true

        const floatData = flattenChunks()
        const buffer = audioContext.createBuffer(1, floatData.length, audioContext.sampleRate)
        buffer.copyToChannel(floatData, 0)

        const wavArrayBuffer = audioBufferToWav(buffer)
        const wavBlob = new Blob([wavArrayBuffer], { type: 'audio/wav' })

        const formData = new FormData()
        formData.append('file', wavBlob, 'voice.wav')

        axios.post(apiStore.getTranscribeApi(), formData)
            .then(async (res) => {
                chunks.value = []
                // 语音转文字
                console.log("获得得文字是：" + res.data.message)
                // 文字调用LLM，不可以使用异步
                const answer = await sendQuestion(res.data.message);
                //
                console.log("获得得回答是：" + answer)
                if (answer.length > 0) {
                    // 处理回答中的控制符
                    const result = extractAndRemoveEPlaceholders(answer);
                    console.log("处理控制符得到：" + result)
                    if (result.placeholders.length > 0) {
                        // 设置表情
                        useModelStore().getModel()?.expression(result.placeholders[0])
                    }
                    // 处理回答中的英文
                    let filteredAnswer = removeEnglishCharacters(result.cleaned);
                    console.log("处理英文得到：" + filteredAnswer)
                    // 文字转语音
                    const arraybuffer = await voiceGenerator(filteredAnswer);

                    // 控制模型嘴部
                    mouthControl(arraybuffer, returnAudioContext, result.cleaned)
                } else {
                    ElMessage.error('LLM的回复为空');
                }
                recorderStore.waitingResponse = false
            })
            .catch(err => {
                console.error('上传失败', err)
                recorderStore.isRecording = false
            })
    }

    function stopRecording() {
        workletNode.value?.disconnect()
        source.value?.disconnect()
        mediaStream.value?.getTracks().forEach(track => track.stop())
        chunks.value = []
        silenceTimer.value && clearTimeout(silenceTimer.value)
        silenceTimer.value = null
    }

    watch(() => recorderStore.isRecording, val => {
        if (val) startRecording()
        else stopRecording()
    })

    return {
        startRecording,
        stopRecording,
    }
}