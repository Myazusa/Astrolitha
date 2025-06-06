package com.github.myazusa.astrolithabackend.common.builder;


import java.util.List;

public class PromptConstructionBuilder {
    private final StringBuilder questionText = new StringBuilder();

    public PromptConstructionBuilder() {
        clear();
    }

    public PromptConstructionBuilder withEmotion(String prompt) {
        questionText.append("可使用的表情有：").append(prompt).append("。");
        return this;
    }

    /**
     * 在回答里不能包含除传入以外的语言
     * @return
     */
    public PromptConstructionBuilder withBanLanguage() {
        questionText.append("回答中不可以包含除简体中文外的文字。");
        return this;
    }

    public PromptConstructionBuilder withEmotions(List<String> emotions) {
        questionText.append("请在回答的最后加上你回答这个问题的心情，并把心情转换为指令，可以使用以下json数组内表情指令：[");
        for (String emotion : emotions) {
            questionText.append("\"{#e");
            questionText.append(emotion);
            questionText.append("}\",");
        }
        questionText.append("]");
        return this;
    }

    /**
     * 更改回答使用的语言
     * @param language 只支持zh和en
     * @return
     */
    public PromptConstructionBuilder withLanguage(String language) {
        switch (language) {
            case "zh" -> questionText.append("请使用繁体中文回答。");
            case "en" -> questionText.append("请使用英文回答。");
            default -> {}
        }
        return this;
    }
    public PromptConstructionBuilder withLanguage() {
        questionText.append("请使用简体中文回答。");
        return this;
    }

    /**
     * 口语化简化回答
     * @return
     */
    public PromptConstructionBuilder withSimplify(){
        questionText.append("请尽可能的简要概述。");
        return this;
    }

    /**
     * 优化。禁止模型随便调用工具
     * @return
     */
    public PromptConstructionBuilder withLimitToolUse(){
        questionText.append("请不要随意调用工具，仅当无法用自己知识回答的时候才调用。");
        return this;
    }

    public String build() {
        return questionText.toString();
    }

    public void clear() {
        questionText.setLength(0);
    }
}
