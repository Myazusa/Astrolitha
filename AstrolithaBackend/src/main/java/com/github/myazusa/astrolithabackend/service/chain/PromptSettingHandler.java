package com.github.myazusa.astrolithabackend.service.chain;

import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;

public class PromptSettingHandler extends QuestionOptionHandler{
    @Override
    protected boolean doHandle(QuestionRequestDTO dto, QuestionOptionResponsibilityChain.ChainContext context) {
        context.getPromptConstructionBuilder()
                .withLanguage()
                .withSimplify();
        return true;
    }
}
