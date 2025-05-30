package com.github.myazusa.astrolithabackend.common.builder;

public class PromptConstructionBuilder {
    private final StringBuilder questionText = new StringBuilder();

    public PromptConstructionBuilder withRag(String prompt) {
        questionText.append("相关的文档内容有：").append(prompt).append("。");
        return this;
    }

    public PromptConstructionBuilder withEmotion(String prompt) {
        questionText.append("可使用的表情有：").append(prompt).append("。");
        return this;
    }

    public PromptConstructionBuilder withBanLanguage() {
        questionText.append("回答中不可以包含除简体中文外的语言。");
        return this;
    }

    public String build(String question) {
        questionText.append(question);
        return questionText.toString();
    }

    public PromptConstructionBuilder clear() {
        questionText.setLength(0);
        return this;
    }
}
