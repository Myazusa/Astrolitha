package com.github.myazusa.astrolithabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParsingFileRequestDTO {
    @NotNull
    @NotBlank
    private String fileName;
}
