package com.github.myazusa.astrolithabackend.service.micro;

import org.springframework.stereotype.Service;

@Service
public class PromptConstructionService {
    public String constructPromptWithText(String prompt, String Text){
        return "相关的文档内容有："+prompt+"。\n用户的问题为："+Text;
    }
}
