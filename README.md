<div align="center">
    <img src="./docs/image/largeicon.png" width="70%" alt="Astrolitha">
<hr>

[![License](https://img.shields.io/badge/LICENSE-AGPL-blue.svg?style=for-the-badge)](https://github.com/Myazusa/Astrolitha/blob/main/LICENSE)
![GitHub last commit](https://img.shields.io/github/last-commit/Myazusa/Astrolitha?style=for-the-badge)
![GitHub Release](https://img.shields.io/github/v/release/Myazusa/Astrolitha?style=for-the-badge)

**English** | [**简体中文**](./docs/cn/README.md)

</div>

## About

A distributed AI integration project.

## Features

- **RAG service:** Provides self-built RAG, Including parsing docx and other files and VDB storage.
- **Live2D display driven by LLM:** Select any Live2D model to display, And the movements and speech are controlled by LLM.
- **Integrate TTS, ARS and LLM:** Call LLM through ARS and use ARS to generate speech at the end. 
- **Distributed support:** Supports deployment using Kubernetes. 

## Installation

### Windows
1. Download [Docker Desktop](https://www.docker.com/products/docker-desktop/).
2. Clone the project at the desired location.
    ```bash
        git clone https://github.com/Myazusa/Astrolitha.git
        cd Astrolitha
    ```
3. Run docker-compose.yaml.
    ```bash
        docker-compose up -d
    ```

### Linux
1. Install Docker Desktop.For details, please see [Docker Docs](https://docs.docker.com/desktop/setup/install/linux/)
2. Set up Docker service to start automatically.
    ```bash
        sudo systemctl enable docker
        sudo systemctl start docker
    ```
3. Check if there is NVIDIA driver.
    ```bash
        nvidia-smi
    ```
4. Install graphics card dependencies.
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
5. Install NVIDIA toolkit and runtime.
    ```bash
      sudo apt-get update
      sudo apt-get install -y nvidia-container-toolkit
      sudo nvidia-ctk runtime configure --runtime=docker
      sudo systemctl restart docker
    ```
6. Clone the project at the desired location.
    ```bash
        git clone https://github.com/Myazusa/Astrolitha.git
        cd Astrolitha
    ```
7. Run docker-compose.yaml.
    ```bash
        docker-compose up -d
    ```
### Mac
1. Promise me you won’t use Mac to run LLM projects, okay?

## Finish
Great, now you can use all the features in your browser.
```bash
    http://localhost:80
```
