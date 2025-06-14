package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.dto.CustomToolFunctionDTO;
import com.github.myazusa.astrolithabackend.service.agent.CustomAgentBuilderService;
import com.github.myazusa.astrolithabackend.service.micro.ToolFunctionSqlService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomAgentCompositionService {
    private final CustomAgentBuilderService customAgentBuilderService;
    private final ToolFunctionSqlService toolFunctionSqlService;

    public CustomAgentCompositionService(CustomAgentBuilderService customAgentBuilderService, ToolFunctionSqlService toolFunctionSqlService) {
        this.customAgentBuilderService = customAgentBuilderService;
        this.toolFunctionSqlService = toolFunctionSqlService;
    }
    public String addTool(CustomToolFunctionDTO customToolFunctionDTO){
        customAgentBuilderService.buildAgent(customToolFunctionDTO);
        return toolFunctionSqlService.addToolFunction(customToolFunctionDTO);
    }
    public List<CustomToolFunctionDTO> listTool(){
        return toolFunctionSqlService.selectAll();
    }
}
