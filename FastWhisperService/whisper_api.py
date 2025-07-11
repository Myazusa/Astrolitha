from faster_whisper import WhisperModel
from fastapi import FastAPI, UploadFile, File
import uvicorn
import os

from voice_process import normalize_audio, denoise_audio, vad_filter

whisper_api = FastAPI()

model_path = "./model/systran/faster-whisper-large-v3"
model_path_v2 = "./model/guillaumekln/faster-whisper-large-v2"
beam_size = 3
# 用gpu运行要使用device=cuda,compute_type=float16，用cpu运行使用device=cpu,compute_type=int8
model = WhisperModel(model_path, device="cpu", compute_type="int8", local_files_only=True)

@whisper_api.post("/transcribe")
async def transcribe(file: UploadFile = File(...)):
    # linux路径的tmp，不是项目下的
    temp_path = f"/tmp/{file.filename}"
    norm_path = f"/tmp/norm_{file.filename}"
    denoise_path = f"/tmp/denoise_{file.filename}"
    vad_path = f"/tmp/vad_{file.filename}"

    # windows下用这个
    # temp_path = f"./tmp/{file.filename}"
    # norm_path = f"./tmp/norm_{file.filename}"
    # denoise_path = f"./tmp/denoise_{file.filename}"
    # vad_path = f"./tmp/vad_{file.filename}"

    # 按顺序处理：响度归一化 -> 降噪 -> VAD
    normalize_audio(temp_path, norm_path)
    denoise_audio(norm_path, denoise_path)
    vad_filter(denoise_path, vad_path)

    # 识别
    segments, _ = model.transcribe(vad_path, beam_size=beam_size,temperature=0.2,language="zh")

    # 删缓存
    os.remove(temp_path)
    os.remove(denoise_path)
    os.remove(norm_path)
    os.remove(vad_path)

    return {"text": "".join([seg.text for seg in segments])}

@whisper_api.get("/transcribe_test")
async def transcribe():
    # linux路径的tmp，不是项目下的
    # temp_path = f"/tmp/{file.filename}"
    # norm_path = f"/tmp/norm_{file.filename}"
    # denoise_path = f"/tmp/denoise_{file.filename}"
    # vad_path = f"/tmp/vad_{file.filename}"

    test_name = "test_voice.wav"
    test_path = "./input/"

    # windows下用这个
    norm_path = f"./tmp/norm_{test_name}"
    denoise_path = f"./tmp/denoise_{test_name}"
    vad_path = f"./tmp/vad_{test_name}"

    # 按顺序处理：响度归一化 -> 降噪 -> VAD
    normalize_audio(test_path+test_name, norm_path)
    denoise_audio(norm_path, denoise_path)
    vad_result_path, is_empty = vad_filter(denoise_path, vad_path)
    if is_empty:
        return {"error": "未检测到语音"}

    # 识别
    segments, _ = model.transcribe(vad_path, beam_size=beam_size,temperature=0.2,language="zh")

    # 不删缓存
    # os.remove(norm_path)
    # os.remove(denoise_path)
    # os.remove(vad_path)

    return {"text": "".join([seg.text for seg in segments])}

if __name__ == "__main__":
    uvicorn.run(whisper_api, host="0.0.0.0", port=10350)