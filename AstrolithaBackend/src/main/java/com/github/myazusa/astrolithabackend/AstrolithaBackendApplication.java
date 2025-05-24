package com.github.myazusa.astrolithabackend;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication
public class AstrolithaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(AstrolithaBackendApplication.class, args);
        // 服务启动需要先运行ollama应用、milvus容器。只要是运行/测试都需要milvus服务存在，否则bean报错
        // prompt是指带有上下文提示的文本段，例如历史回答+额外人设+当前用户提问才组成一个完整的prompt
        log.info("服务已启动");
    }

}
