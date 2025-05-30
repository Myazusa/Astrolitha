package com.github.myazusa.astrolithabackend.common.exception;

public class JsonConversionException extends RuntimeException{
    public JsonConversionException() {
        super("Json文件转换错误");
    }
}
