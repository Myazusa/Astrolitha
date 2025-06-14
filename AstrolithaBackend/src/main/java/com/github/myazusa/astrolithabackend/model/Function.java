package com.github.myazusa.astrolithabackend.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class Function {
    private String functionName;

    private String toolDescription;

    private String remoteApi;

    private String requestMethod;
}
