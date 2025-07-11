FROM nvidia/cuda:12.9.0-cudnn-runtime-ubuntu24.04

WORKDIR /root

ENV DEBIAN_FRONTEND=noninteractive

RUN echo "deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ noble main restricted universe multiverse\n\
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ noble-updates main restricted universe multiverse\n\
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ noble-backports main restricted universe multiverse\n\
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ noble-security main restricted universe multiverse" \
> /etc/apt/sources.list && \
rm -rf /etc/apt/sources.list.d/* && \
apt-get update -o Acquire::http::Pipeline-Depth=0 && \
apt-get install -y python3 python3-pip ffmpeg git && \
apt-get clean

RUN apt-get update && apt-get install -y python3 python3-venv
RUN python3 -m venv /venv

COPY requirements.txt /app/requirements.txt
RUN /venv/bin/pip install -i https://mirrors.aliyun.com/pypi/simple/ --upgrade pip && /venv/bin/pip install -i https://mirrors.aliyun.com/pypi/simple/ -r /app/requirements.txt

ENV PATH="/venv/bin:$PATH"

WORKDIR /app
COPY model ./model
COPY rnnoise-models ./rnnoise-models
COPY voice_process.py .
COPY whisper_api.py .

CMD ["python", "whisper_api.py","--host", "0.0.0.0", "--port", "10350"]