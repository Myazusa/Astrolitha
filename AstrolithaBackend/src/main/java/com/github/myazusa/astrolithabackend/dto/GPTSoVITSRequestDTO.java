package com.github.myazusa.astrolithabackend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class GPTSoVITSRequestDTO {
    // 要读的文本
    @NotNull
    private String text;
    @NotNull
    private String text_lang;
    @NotNull
    private String ref_audio_path;
    @NotNull
    private String prompt_text;
    @NotNull
    private String prompt_lang;
}
