package com.github.myazusa.astrolithabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequestDTO {
    @NotNull
    @NotBlank
    private String modelInterface;

    @NotNull
    @NotBlank
    private String question;

    @NotNull
    private Boolean enableAgent;

    @NotNull
    private Boolean enableCustomAgent;

    private List<String> emotions;
}
