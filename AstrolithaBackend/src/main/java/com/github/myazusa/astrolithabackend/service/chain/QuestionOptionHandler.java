package com.github.myazusa.astrolithabackend.service.chain;

import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;

public abstract class QuestionOptionHandler {
    protected QuestionOptionHandler next;

    public QuestionOptionHandler setNext(QuestionOptionHandler next) {
        this.next = next;
        return next;
    }

    public void handle(QuestionRequestDTO dto,QuestionOptionResponsibilityChain.ChainContext context) {
        // 返回false会中断，处于这个后面的责任链都不干了
        if (doHandle(dto,context) && next != null) {
            next.handle(dto,context);
        }
    }
    // 返回true以处理下一个
    protected abstract boolean doHandle(QuestionRequestDTO dto, QuestionOptionResponsibilityChain.ChainContext context);
}
