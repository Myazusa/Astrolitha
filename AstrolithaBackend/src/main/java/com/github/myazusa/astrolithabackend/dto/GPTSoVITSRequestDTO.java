package com.github.myazusa.astrolithabackend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class GPTSoVITSRequestDTO {
    // 提示语音的路径
    @NotNull
    private String ref_audio_path;

    @NotNull
    // 提示语音对应的文本
    private String prompt_text;

    @NotNull
    // 文本的语言
    private String text_lang;

    // 提示语音的语言
    @NotNull
    private String prompt_lang;

    // 要读的文本
    @NotNull
    private String text;

    // 随机范围最高值
    private Float top_k;

    // 随机范围最低阈值
    private Float top_p;

    // 模型温度
    private Float temperature;

    // 音频速度
    private Float speed;
}
