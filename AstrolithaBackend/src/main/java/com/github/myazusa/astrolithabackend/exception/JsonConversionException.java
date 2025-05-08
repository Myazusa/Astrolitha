package com.github.myazusa.astrolithabackend.exception;

public class JsonConversionException extends RuntimeException{
    public JsonConversionException() {
        super("Json文件转换错误");
    }
}
