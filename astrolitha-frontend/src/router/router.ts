import {createRouter, createWebHistory, RouteRecordRaw} from "vue-router";

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        component: ()=>import("@/App.vue"),
        children:[
            {
                path:'/',
                component: ()=>import("@/pages/UserLogin.vue"),
            }
        ]
    }
]

const router = createRouter({
    end: undefined, sensitive: undefined, strict: undefined,
    history: createWebHistory('/'),
    routes,
    scrollBehavior(to){
        if(to.hash){
            return{
                el:to.hash,
                behavior: 'smooth', /*平滑滚动到标签*/
            };
        };
    }
});

export default router;