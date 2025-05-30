package com.github.myazusa.astrolithabackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RenameRequestDTO {
    @NotNull
    String newName;
    @NotNull
    String oldName;
}
