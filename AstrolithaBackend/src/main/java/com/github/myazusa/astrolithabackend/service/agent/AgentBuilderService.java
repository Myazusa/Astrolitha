package com.github.myazusa.astrolithabackend.service.agent;

import com.github.myazusa.astrolithabackend.service.QueryVDBCompositionService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AgentBuilderService {
    private final ObjectProvider<QueryVDBCompositionService> queryVDBCompositionServiceObjectProvider;


    @Autowired
    public AgentBuilderService(ObjectProvider<QueryVDBCompositionService> queryVDBCompositionServiceObjectProvider) {
        this.queryVDBCompositionServiceObjectProvider = queryVDBCompositionServiceObjectProvider;
    }

    public AgentBuilder builder() {
        QueryVDBCompositionService queryVDBCompositionService = queryVDBCompositionServiceObjectProvider.getIfAvailable();
        if (queryVDBCompositionService == null) {
            return null;
        }
        return new AgentBuilder(queryVDBCompositionService);
    }

    public static class AgentBuilder {
        private final QueryVDBCompositionService queryVDBCompositionService;
        private final List<ToolCallback> toolCallbacks = new ArrayList<>();

        public AgentBuilder(QueryVDBCompositionService queryVDBCompositionService) {
            this.queryVDBCompositionService = queryVDBCompositionService;
            this.clear();
        }

        private void addAgent(ToolCallback toolCallback) {
            toolCallbacks.add(toolCallback);
        }

        private void addAgent(ToolCallback[] toolCallbackArray) {
            toolCallbacks.addAll(Arrays.stream(toolCallbackArray).toList());
        }

        public AgentBuilder withKnowledgeBaseAgent(){
            ToolCallback[] toolCallbacks = ToolCallbacks.from(new KnowledgeBaseAgent(queryVDBCompositionService));
            this.addAgent(toolCallbacks);
            return this;
        }

        public AgentBuilder withUtilsAgent() {
            ToolCallback[] toolCallbacks = ToolCallbacks.from(new UtilsAgent());
            this.addAgent(toolCallbacks);
            return this;
        }

        public List<ToolCallback> build(){
            return toolCallbacks;
        }
        public void clear() {
            toolCallbacks.clear();
        }
    }
}
