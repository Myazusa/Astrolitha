FROM nvidia/cuda:12.3.2-cudnn9-runtime-ubuntu22.04

WORKDIR /root

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y \
    python3 python3-pip ffmpeg git \
    && apt-get clean

COPY requirements.txt /app/requirements.txt
RUN pip3 install --upgrade pip
RUN pip3 install -r /app/requirements.txt

WORKDIR /app
COPY whisper_api.py .

CMD ["python", "whisper_api.py","--host", "0.0.0.0", "--port", "10350"]