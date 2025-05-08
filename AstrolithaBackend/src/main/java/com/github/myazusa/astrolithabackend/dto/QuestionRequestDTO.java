package com.github.myazusa.astrolithabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionRequestDTO {
    @NotNull
    @NotBlank
    private String modelInterface;

    @NotNull
    @NotBlank
    private String question;
}
