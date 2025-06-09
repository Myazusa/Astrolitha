package com.github.myazusa.astrolithabackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Accessors(chain = true)
@Data
public class CustomToolFunctionRequestDTO {
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
    private List<Param> params;

    @NotNull
    @NotBlank
    private String returnValue;

    @Accessors(chain = true)
    @Data
    public static class Param {
        private String paramName;
        private String paramDescription;
    }

}
