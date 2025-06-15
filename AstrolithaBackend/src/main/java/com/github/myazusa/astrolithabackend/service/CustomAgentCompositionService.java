package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.dto.CustomToolFunctionDTO;
import com.github.myazusa.astrolithabackend.service.agent.CustomAgentBuilderService;
import com.github.myazusa.astrolithabackend.service.micro.ToolFunctionSqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomAgentCompositionService {
    private final CustomAgentBuilderService customAgentBuilderService;
    private final ToolFunctionSqlService toolFunctionSqlService;

    public CustomAgentCompositionService(CustomAgentBuilderService customAgentBuilderService, ToolFunctionSqlService toolFunctionSqlService) {
        this.customAgentBuilderService = customAgentBuilderService;
        this.toolFunctionSqlService = toolFunctionSqlService;
    }
    public void addTool(CustomToolFunctionDTO customToolFunctionDTO){
        toolFunctionSqlService.addToolFunction(customToolFunctionDTO);
    }
    public List<CustomToolFunctionDTO> listTool(){
        return toolFunctionSqlService.selectAll();
    }
    @Transactional
    public void enableTool(CustomToolFunctionDTO customToolFunctionDTO){
        customAgentBuilderService.buildAgent(customToolFunctionDTO);
        toolFunctionSqlService.updateToolEnabled(customToolFunctionDTO);
    }
}
