<script setup lang="ts">
import {useUserCenterOption} from "@/store/UserCenterStore";

const userCenterOption = useUserCenterOption();

</script>

<template>
  <div class="option-root">
    <div class="card-grid">
      <el-card class="card" shadow="never">
        <div class="section-title">Astrolitha設置</div>
        <el-form label-width="7rem" class="form">
          <el-form-item label="後端IP地址">
            <el-input v-model="userCenterOption.backendHost" placeholder="127.0.0.1" style="width: 12rem" />
          </el-form-item>
          <el-form-item label="後端端口">
            <el-input v-model="userCenterOption.backendPort" placeholder="80" style="width: 5rem" />
          </el-form-item>
          <el-form-item label="本地化TTS">
            <el-switch v-model="userCenterOption.ttsLocalMode" :disabled="true"/>
          </el-form-item>
          <el-form-item label="本地化ARS">
            <el-switch v-model="userCenterOption.arsLocalMode" :disabled="true"/>
          </el-form-item>
          <el-form-item label="本地化VDB">
            <el-switch v-model="userCenterOption.vdbLocalMode" :disabled="true"/>
          </el-form-item>
          <el-form-item label="本地化RDB">
            <el-switch v-model="userCenterOption.rdbLocalMode" :disabled="true"/>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="card" shadow="never">
        <div class="section-title">模型設置</div>
        <el-form label-width="10rem" class="form">
          <el-form-item label="Chat模型">
            <el-select v-model="userCenterOption.chatModel" style="width: 12rem">
              <el-option :value="userCenterOption.chatModel" />
            </el-select>
          </el-form-item>
          <el-form-item label="Embedding模型">
            <el-select v-model="userCenterOption.embeddingModel" style="width: 12rem">
              <el-option :value="userCenterOption.embeddingModel" />
            </el-select>
          </el-form-item>
          <el-form-item label="ToolCall模型">
            <el-select v-model="userCenterOption.toolCallModel" style="width: 12rem">
              <el-option :value="userCenterOption.toolCallModel" />
            </el-select>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="card" shadow="never">
        <div class="section-title">其他設置</div>
        <el-form label-width="10rem" class="form">
          <el-form-item label="語言設置">
            <el-select v-model="userCenterOption.language" style="width: 8rem">
              <el-option label="繁體中文" :value="userCenterOption.language" />
            </el-select>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="card" shadow="never">
        <div class="section-title" style="margin-top:2rem;">集群設置</div>
        <el-form label-width="7rem" class="form">
          <el-form-item label="集群模式">
            <el-switch v-model="userCenterOption.clusterMode" />
          </el-form-item>
          <div v-if="userCenterOption.clusterMode">
            <el-form-item label="TTS集群化">
              <el-switch v-model="userCenterOption.ttsMode" />
            </el-form-item>
            <div v-if="userCenterOption.ttsMode">
              <el-form-item label="TTS服務IP地址">
                <el-input v-model="userCenterOption.ttsHost" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="TTS服務端口號">
                <el-input v-model="userCenterOption.ttsPort" placeholder="10350" style="width: 5rem" />
              </el-form-item>
            </div>
            <el-form-item label="ARS集群化">
              <el-switch v-model="userCenterOption.arsMode" />
            </el-form-item>
            <div v-if="userCenterOption.arsMode">
              <el-form-item label="ARS服務IP地址">
                <el-input v-model="userCenterOption.arsHost" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="ARS服務端口號">
                <el-input v-model="userCenterOption.arsPort" placeholder="10500" style="width: 5rem" />
              </el-form-item>
            </div>
            <el-form-item label="VDB集群化">
              <el-switch v-model="userCenterOption.vdbMode" />
            </el-form-item>
            <div v-if="userCenterOption.vdbMode">
              <el-form-item label="VDB服務IP地址">
                <el-input v-model="userCenterOption.vdbHost" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="VDB服務端口號">
                <el-input v-model="userCenterOption.vdbPort" placeholder="9091" style="width: 5rem" />
              </el-form-item>
            </div>
            <el-form-item label="RDB集群化">
              <el-switch v-model="userCenterOption.rdbMode" />
            </el-form-item>
            <div v-if="userCenterOption.vdbMode">
              <el-form-item label="RDB服務IP地址">
                <el-input v-model="userCenterOption.rdbHost" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="RDB服務端口號">
                <el-input v-model="userCenterOption.rdbPort" placeholder="3306" style="width: 5rem" />
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-card>

    </div>
  </div>
</template>

<style scoped>
.option-root {
  padding: 2rem;
  min-height: 100vh;
  background: var(--theme-color-primary);
}
.card-grid {
  columns: 2 1rem;
  column-gap: 1rem;
}
.card {
  background: var(--theme-color-secondary);
  border-radius: 1rem;
  padding: 0.5rem 1rem;
  border: none;
  margin-bottom: 1rem;
  min-width: 0;
}
.section-title {
  font-size: 1.5rem;
  color: var(--theme-color-hover);
  margin-bottom: 1rem;
}
.form {
  font-size: 1rem;
}
@media (max-width: 57rem) {
  .card-grid {
    columns: 1 1rem;
  }
}
</style>
