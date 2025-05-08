package com.github.myazusa.astrolithabackend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class GPTSoVITSRequestDTO {
    // 返回的音频文件名
    @NotNull
    private String refer_wav_path;

    // 提示词
    private String prompt_text;

    // 语言
    @NotNull
    private String prompt_language;

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
