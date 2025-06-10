package com.github.myazusa.astrolithabackend.common.inject;

import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.common.exception.UnknownException;
import jakarta.annotation.Nullable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * ToolCallback的方法体
 */
public class ToolMethod {
    public static String invoke(String remoteApi, String requestMethod) {
        WebClient webClient = WebClient.builder().baseUrl(remoteApi).build();
        CompletableFuture<String> future = null;
        if (Objects.equals(requestMethod, "post")) {
            future = webClient.post()
                    .retrieve()
                    .bodyToMono(String.class)
                    .toFuture();
        }else if (Objects.equals(requestMethod, "get")) {
            future = webClient.get().retrieve().bodyToMono(String.class).toFuture();
        }

        String result;
        try {
            if (future != null) {
                result = future.get();
                return result;
            }else {
                throw new UnknownException("future为空");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RemoteServiceException("自定义的远端服务未响应：" + e.getMessage());
        }
    }
}
