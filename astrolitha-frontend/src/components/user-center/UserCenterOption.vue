<script setup lang="ts">
import {useUserCenterOption} from "@/store/UserCenterStore";

const userCenterOption = useUserCenterOption();

// 获取所有需要的响应式引用
const backendHost = userCenterOption.getBackendHostRef()
const backendPort = userCenterOption.getBackendPortRef()
const ttsLocalMode = userCenterOption.getTtsLocalModeRef()
const arsLocalMode = userCenterOption.getArsLocalModeRef()
const vdbLocalMode = userCenterOption.getVdbLocalModeRef()
const rdbLocalMode = userCenterOption.getRdbLocalModeRef()
const clusterMode = userCenterOption.getClusterModeRef()
const ttsMode = userCenterOption.getTtsModeRef()
const ttsHost = userCenterOption.getTtsHostRef()
const ttsPort = userCenterOption.getTtsPortRef()
const arsMode = userCenterOption.getArsModeRef()
const arsHost = userCenterOption.getArsHostRef()
const arsPort = userCenterOption.getArsPortRef()
const vdbMode = userCenterOption.getVdbModeRef()
const vdbHost = userCenterOption.getVdbHostRef()
const vdbPort = userCenterOption.getVdbPortRef()
const language = userCenterOption.getLanguageRef()
</script>

<template>
  <div class="option-root">
    <div class="card-grid">
      <el-card class="card" shadow="never">
        <div class="section-title">Astrolitha設置</div>
        <el-form label-width="7rem" class="form">
          <el-form-item label="後端IP地址">
            <el-input v-model="backendHost" placeholder="127.0.0.1" style="width: 12rem" />
          </el-form-item>
          <el-form-item label="後端端口">
            <el-input v-model="backendPort" placeholder="80" style="width: 5rem" />
          </el-form-item>
          <el-form-item label="本地化TTS">
            <el-switch v-model="ttsLocalMode" />
          </el-form-item>
          <el-form-item label="本地化ARS">
            <el-switch v-model="arsLocalMode" />
          </el-form-item>
          <el-form-item label="本地化VDB">
            <el-switch v-model="vdbLocalMode" />
          </el-form-item>
          <el-form-item label="本地化RDB">
            <el-switch v-model="rdbLocalMode" />
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="card" shadow="never">
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
                <el-input v-model="ttsHost" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="TTS服務端口號">
                <el-input v-model="ttsPort" placeholder="10350" style="width: 5rem" />
              </el-form-item>
            </div>
            <el-form-item label="ARS集群化">
              <el-switch v-model="arsMode" />
            </el-form-item>
            <div v-if="arsMode === true">
              <el-form-item label="ARS服務IP地址">
                <el-input v-model="arsHost" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="ARS服務端口號">
                <el-input v-model="arsPort" placeholder="10500" style="width: 5rem" />
              </el-form-item>
            </div>
            <el-form-item label="VDB集群化">
              <el-switch v-model="vdbMode" />
            </el-form-item>
            <div v-if="vdbMode === true">
              <el-form-item label="VDB服務IP地址">
                <el-input v-model="vdbHost" placeholder="127.0.0.1" style="width: 12rem" />
              </el-form-item>
              <el-form-item label="VDB服務端口號">
                <el-input v-model="vdbPort" placeholder="9091" style="width: 5rem" />
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-card>
      <el-card class="card" shadow="never">
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
