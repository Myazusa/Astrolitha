package com.github.myazusa.astrolithabackend.service.chain;

import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;
import org.springframework.stereotype.Component;

@Component
public class EmotionHandler extends QuestionOptionHandler{
    @Override
    protected boolean doHandle(QuestionRequestDTO dto, QuestionOptionResponsibilityChain.ChainContext context) {
        if (dto.getEmotions() != null && !dto.getEmotions().isEmpty()) {
            context.getPromptConstructionBuilder().withEmotions(dto.getEmotions());
        }
        return true;
    }
}
