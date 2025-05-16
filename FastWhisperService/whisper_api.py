from faster_whisper import WhisperModel
from fastapi import FastAPI, UploadFile, File
import uvicorn
import os

whisper_api = FastAPI()

model_size = "large-v3"
beam_size = 5
# 用gpu运行要使用float16，用cpu运行使用int8
model = WhisperModel(model_size, device="cuda", compute_type="float16")

@whisper_api.post("/transcribe")
async def transcribe(file: UploadFile = File(...)):
    temp_path = f"/tmp/{file.filename}"
    with open(temp_path, "wb") as f:
        f.write(await file.read())

    segments, _ = model.transcribe(temp_path, beam_size=beam_size,temperature=0)
    os.remove(temp_path)
    return {"text": "".join([seg.text for seg in segments])}

if __name__ == "__main__":
    uvicorn.run(whisper_api, host="0.0.0.0", port=10350)