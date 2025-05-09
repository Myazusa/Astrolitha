import {createApp} from "vue";
import App from "@/App.vue";
import router from "@/router/router";
import ElementPlus from "element-plus"
import {createPinia} from "pinia";

createApp(App)
    .use(router)
    .use(createPinia())
    .use(ElementPlus)
    .mount('#app')