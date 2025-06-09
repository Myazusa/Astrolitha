package com.github.myazusa.astrolithabackend.common.inject;

import com.github.myazusa.astrolithabackend.dto.CustomToolFunctionRequestDTO;
import net.bytebuddy.implementation.bind.annotation.AllArguments;

import java.util.List;

public class ToolMethodDelegate {
    private final String remoteApi;
    private final String requestMethod;
    private final List<CustomToolFunctionRequestDTO.Param> params;
    public ToolMethodDelegate(String remoteApi, String requestMethod, List<CustomToolFunctionRequestDTO.Param> params){
        this.remoteApi = remoteApi;
        this.requestMethod = requestMethod;
        this.params = params;
    }
    public Object delegate(@AllArguments Object[] args) {
        return ToolMethod.invoke(remoteApi,requestMethod,params);
    }
}
