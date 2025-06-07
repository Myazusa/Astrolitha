<div align="center">
    <img src="../image/largeicon.png" width="70%" alt="Astrolitha">
<hr>

[![License](https://img.shields.io/badge/LICENSE-AGPL-blue.svg?style=for-the-badge)](https://github.com/Myazusa/Astrolitha/blob/main/LICENSE)
![GitHub last commit](https://img.shields.io/github/last-commit/Myazusa/Astrolitha?style=for-the-badge)
![GitHub Release](https://img.shields.io/github/v/release/Myazusa/Astrolitha?style=for-the-badge)

[**English**](../../README.md) | **简体中文**

</div>

## 关于项目

这是一个分布式AI集成项目

## 功能

- **个人知识库：** 提供RAG系列管理工具，包含docx等文件的解析和VDB的存储。
- **LLM驱动的Live2D：** 可选择任意Live2D模型进行使用，动作和说话均由大模型控制。
- **集成TTS、ARS和LLM：** 通过语音转文字服务调用大模型，最后用文字转语音服务生成语音给用户。
- **支持Agent：** 大模型可以根据用户给出的需求，进行函数调用来选择合适的工具做出对应操作。
- **支持集群部署：** 完全容器化的服务支持Kubernetes进行集群部署。 

## 安装

### Windows
1. 下载 [Docker Desktop](https://www.docker.com/products/docker-desktop/).
2. 克隆项目至本地
    ```bash
        git clone https://github.com/Myazusa/Astrolitha.git
        cd Astrolitha
    ```
3. 运行 docker-compose.yaml
    ```bash
        docker-compose up -d
    ```

### Linux
1. 安装 Docker Desktop。具体方法请参考 [Docker Docs](https://docs.docker.com/desktop/setup/install/linux/)
2. 设置docker自启动
    ```bash
        sudo systemctl enable docker
        sudo systemctl start docker
    ```
3. 检查是否安装了NVIDIA驱动，安装了就不用继续了
    ```bash
        nvidia-smi
    ```
4. 安装显卡依赖
    ```bash
        sudo apt-get update
        sudo apt-get install -y curl gnupg ca-certificates
    
    distribution=$(. /etc/os-release;echo $ID$VERSION_ID) \
      && curl -s -L https://nvidia.github.io/libnvidia-container/gpgkey | \
         sudo gpg --dearmor -o /etc/apt/keyrings/nvidia-container-toolkit.gpg \
      && curl -s -L https://nvidia.github.io/libnvidia-container/$distribution/libnvidia-container.list | \
         sed 's#deb #deb [signed-by=/etc/apt/keyrings/nvidia-container-toolkit.gpg] #' | \
         sudo tee /etc/apt/sources.list.d/nvidia-container-toolkit.list
    ```
5. 安装NVIDIA工具包和运行时环境
    ```bash
      sudo apt-get update
      sudo apt-get install -y nvidia-container-toolkit
      sudo nvidia-ctk runtime configure --runtime=docker
      sudo systemctl restart docker
    ```
6. 克隆项目至本地
    ```bash
        git clone https://github.com/Myazusa/Astrolitha.git
        cd Astrolitha
    ```
7. 运行docker-compose.yaml
    ```bash
        docker-compose up -d
    ```
### Mac
1. 答应我不要用Mac来跑任何大模型相关的项目好么？

## 结果
现在可以使用浏览器访问以下地址来使用功能
```bash
    http://localhost:80
```

## 微服务
项目使用了以下docker镜像来作为微服务

- mysql:latest
- ollama/ollama:latest
- whisper:latest
- breakstring/gpt-sovits:v4
- milvusdb/etcd:latest
- minio/minio:latest
- milvus-standalone
- elasticsearch:8.18.1

以上对应的开源项目地址如下

- [mysql](https://github.com/mysql/mysql-server)
- [ollama](https://github.com/ollama/ollama)
- [whisper](https://github.com/openai/whisper)
- [gpt-sovits](https://github.com/RVC-Boss/GPT-SoVITS)
- [milvus](https://github.com/milvus-io/milvus)
- [elasticsearch](https://github.com/elastic/elasticsearch)

在使用elasticsearch时，如果希望支持中文搜索，请进入容器内运行以下指令然后重启容器
```bash
  ./bin/elasticsearch-plugin install --batch https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-8.18.1.zip
```

## 开源许可
本项目为成品应用而不是工具库或框架，因此使用AGPLv3开源协议而不是MIT或Apache2.0

- 关于个人：完全支持个人使用，且不做任何限制，你可以任意修改源代码且不需要公开。
- 关于商用：你可以随意使用不含修改的原始项目进行对外提供服务，并且获得的收益归你自己所有。如果不得不修改项目，需要同时开源公布修改的代码，如果闭源不公布则需向源项目作者申请授权。

如果遇到问题，欢迎在本项目提出issue，或是成为项目贡献者！