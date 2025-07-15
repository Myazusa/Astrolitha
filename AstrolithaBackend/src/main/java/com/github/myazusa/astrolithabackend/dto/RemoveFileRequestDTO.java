package com.github.myazusa.astrolithabackend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RemoveFileRequestDTO {
    @NotBlank
    @NotNull
    String fileName;
}
