import subprocess
from pydub import AudioSegment
import webrtcvad

# 这个是在嘈杂环境音中使用的
RNNOISE_MODEL_SH = "./rnnoise-models/sh.rnnn"
# 这个是在嘈杂人声中使用的
RNNOISE_MODEL_LQ = "./rnnoise-models/lq.rnnn"

def normalize_audio(input_path, output_path, target_dBFS=-20.0):
    """
    音量归一化：把响度统一到 target_dBFS
    """
    audio = AudioSegment.from_file(input_path)
    change_in_dBFS = target_dBFS - audio.dBFS
    print(f"[Normalize] 原始响度: {audio.dBFS:.2f} dBFS, 调整: {change_in_dBFS:.2f} dB")
    normalized_audio = audio.apply_gain(change_in_dBFS)
    normalized_audio.export(output_path, format="wav")
    return output_path

def denoise_audio(input_path, output_path):
    """
    ffmpeg + arnndn 模型降噪，并转16kHz单声道
    """
    subprocess.run([
        "ffmpeg", "-y", "-i", input_path,
        "-af", f"arnndn=m={RNNOISE_MODEL_LQ}",
        "-ac", "1", "-ar", "16000",
        output_path
    ], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    return output_path

def vad_filter(input_path, output_path, aggressiveness=3):
    """
    使用 webrtcvad 去除静音段
    """
    vad = webrtcvad.Vad(aggressiveness)
    audio = AudioSegment.from_wav(input_path)

    if audio.channels != 1:
        audio = audio.set_channels(1)

    sample_rate = audio.frame_rate
    sample_width = audio.sample_width

    frame_duration = 30  # ms
    frame_size = int(sample_rate * frame_duration / 1000) * sample_width

    speech_chunks = []
    for i in range(0, len(audio.raw_data), frame_size):
        frame = audio.raw_data[i:i+frame_size]
        if len(frame) < frame_size:
            break
        is_speech = vad.is_speech(frame, sample_rate)
        if is_speech:
            start_ms = int(i / sample_width / sample_rate * 1000)
            end_ms = start_ms + frame_duration
            speech_chunks.append(audio[start_ms:end_ms])

    if speech_chunks:
        filtered_audio = sum(speech_chunks, AudioSegment.silent(duration=0))
        filtered_audio.export(output_path, format="wav")
        return output_path, False
    else:
        return None, True
