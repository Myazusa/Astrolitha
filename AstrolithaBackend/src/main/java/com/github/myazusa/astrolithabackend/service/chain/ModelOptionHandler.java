package com.github.myazusa.astrolithabackend.service.chain;

import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;
import org.springframework.stereotype.Component;

@Component
public class ModelOptionHandler extends QuestionOptionHandler{
    @Override
    protected boolean doHandle(QuestionRequestDTO dto, QuestionOptionResponsibilityChain.ChainContext context) {
        if (dto.getTemperature() == null  || Double.isNaN(dto.getTemperature())) {
            dto.setTemperature(0.4);
        } else if (dto.getTemperature() > 1.0 || dto.getTemperature() < 0) {
            dto.setTemperature(0.4);
        }
        return true;
    }
}
