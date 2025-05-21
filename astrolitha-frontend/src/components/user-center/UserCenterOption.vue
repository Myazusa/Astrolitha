<script setup lang="ts">
import { ref } from 'vue'

// todo:把变量设置到store里
// 系统设置
const virtualNetwork = ref(false)
const systemProxy = ref(true)
const autoStart = ref(true)
const silentStart = ref(true)

// 应用设置
const clusterMode = ref(false)
const ttsMode = ref(false)
const arsMode = ref(false)
const vdbMode = ref(false)
const unifiedExit = ref(true)
const logLevel = ref('info')

// 基础设置
const language = ref('zh')
const themeMode = ref('dark')
const trayClickEvent = ref('showMain')
const envType = ref('PowerShell')
const startupPage = ref('proxy')
const startupScript = ref('')
</script>

<template>
  <div class="option-root">
    <div class="card-grid">
      <el-card class="card" shadow="hover">
        <div class="section-title">Astrolitha設置</div>
        <el-form label-width="7rem" class="form">
          <el-form-item label="後端IP地址">
            <el-input v-model="startupScript" placeholder="127.0.0.1" style="width: 12rem" />
          </el-form-item>
          <el-form-item label="後端端口">
            <el-input v-model="startupScript" placeholder="80" style="width: 5rem" />
          </el-form-item>
          <el-form-item label="本地化TTS">
            <el-switch v-model="systemProxy" />
          </el-form-item>
          <el-form-item label="本地化ARS">
            <el-switch v-model="autoStart" />
          </el-form-item>
          <el-form-item label="本地化VDB">
            <el-switch v-model="silentStart" />
          </el-form-item>
          <el-form-item label="本地化RDB">
            <el-switch v-model="silentStart" />
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="card" shadow="hover">
        <div class="section-title" style="margin-top:2rem;">集群設置</div>
        <el-form label-width="7rem" class="form">
          <el-form-item label="集群模式">
            <el-switch v-model="clusterMode" />
          </el-form-item>
          <div v-if="clusterMode === true">
            <el-form-item label="TTS集群化">
              <el-switch v-model="ttsMode" />
            </el-form-item>
            <div v-if="ttsMode === true">
              <el-form-item label="TTS服務IP地址">
                <el-input v-model="startupScript" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="TTS服務端口號">
                <el-input v-model="startupScript" placeholder="10350" style="width: 5rem" />
              </el-form-item>
            </div>
            <el-form-item label="ARS集群化">
              <el-switch v-model="arsMode" />
            </el-form-item>
            <div v-if="arsMode === true">
              <el-form-item label="ARS服務IP地址">
                <el-input v-model="startupScript" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="ARS服務端口號">
                <el-input v-model="startupScript" placeholder="10500" style="width: 5rem" />
              </el-form-item>
            </div>
            <el-form-item label="VDB集群化">
              <el-switch v-model="vdbMode" />
            </el-form-item>
            <div v-if="vdbMode === true">
              <el-form-item label="ARS服務IP地址">
                <el-input v-model="startupScript" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="ARS服務端口號">
                <el-input v-model="startupScript" placeholder="10500" style="width: 5rem" />
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-card>
      <el-card class="card" shadow="hover">
        <div class="section-title">其他設置</div>
        <el-form label-width="10rem" class="form">
          <el-form-item label="語言設置">
            <el-select v-model="language" style="width: 8rem">
              <el-option label="繁體中文" value="zh" />
            </el-select>
          </el-form-item>
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
