package com.github.myazusa.astrolithabackend.service.chain;

import com.github.myazusa.astrolithabackend.common.enums.ModelInterfaceEnums;
import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ModelInterfaceHandler extends QuestionOptionHandler{
    @Override
    protected boolean doHandle(QuestionRequestDTO dto, QuestionOptionResponsibilityChain.ChainContext context) {
        ModelInterfaceEnums modelInterfaceEnums = null;
        try {
            modelInterfaceEnums = ModelInterfaceEnums.getFromString(dto.getModelInterface());
        }catch (Exception e){
            log.error("没有传入的类型：{}",dto.getModelInterface());
        }
        if (modelInterfaceEnums == null) {
            dto.setModelInterface(ModelInterfaceEnums.ollama.toString());
        }
        if (modelInterfaceEnums == ModelInterfaceEnums.python) {
            dto.setModelInterface(ModelInterfaceEnums.ollama.toString());
        }
        return true;
    }
}
