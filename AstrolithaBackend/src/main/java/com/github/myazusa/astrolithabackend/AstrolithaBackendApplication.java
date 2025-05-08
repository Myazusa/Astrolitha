package com.github.myazusa.astrolithabackend;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AstrolithaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(AstrolithaBackendApplication.class, args);
        // 服务启动需要运行ollama应用、milvus容器。
        // prompt是指带有上下文提示的文本段，例如历史回答+额外人设+当前用户提问才组成一个完整的prompt
        log.info("服务已启动");
    }

}
