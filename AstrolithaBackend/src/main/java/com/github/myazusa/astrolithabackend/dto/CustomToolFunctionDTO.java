package com.github.myazusa.astrolithabackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Accessors(chain = true)
@Data
public class CustomToolFunctionDTO {
    @NotNull
    @NotBlank
    private String functionName;

    private String toolDescription;

    @NotNull
    @NotBlank
    private String remoteApi;

    @NotNull
    @NotBlank
    private String requestMethod;

    @NotNull
    private String name;

    @NotNull
    private Boolean enabled;

    @NotNull
    private String toolUUID;

//    @NotNull
//    private List<Param> params;

//    @Accessors(chain = true)
//    @Data
//    public static class Param {
//        private String paramName;
//        private String paramDescription;
//    }

}
