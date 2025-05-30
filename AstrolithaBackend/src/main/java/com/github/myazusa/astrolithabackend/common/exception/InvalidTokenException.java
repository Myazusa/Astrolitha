package com.github.myazusa.astrolithabackend.common.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("无效令牌");
    }
}
