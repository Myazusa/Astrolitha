package com.github.myazusa.astrolithabackend.service.agent;

import com.github.myazusa.astrolithabackend.service.QueryVDBCompositionService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;
public class KnowledgeBaseAgent {
    private final QueryVDBCompositionService queryVDBCompositionService;

    public KnowledgeBaseAgent(QueryVDBCompositionService queryVDBCompositionService) {
        this.queryVDBCompositionService = queryVDBCompositionService;
    }

    @Tool(name = "searchKnowledgeBase", description = "查询知识库的工具方法。仅当用户的问题需要特定的知识库知识，例如文档内容、政策时才使用此工具。对于常识性问题、聊天对话、推理类问题，请不要调用此工具。")
    public String searchKnowledgeBase(@ToolParam(description = "用户的问题") String question) {
        List<String> result = queryVDBCompositionService.queryVDB(question);
        if (!result.isEmpty()) {
            return "查询到的资料如下，请结合以下资料回答问题：\n" + String.join("\n---\n", result);
        }else {
            return "知识库中没有查询到相关的结果";
        }

    }

//    @Tool(name = "queryCurrentSeatNumber", description = "查询餐厅业务的工具方法。查询并返回用户的座位号")
//    public String queryCurrentSeatNumber(){
//        return "座位号为：114514";
//    }
}
