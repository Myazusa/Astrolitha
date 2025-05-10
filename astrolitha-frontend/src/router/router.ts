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
    },
    {
        path:'/uc',
        name: 'UserCenter',
        component: ()=>import("@/pages/UserCenter.vue"),
    },
    {
        path: '/l2d',
        name: 'Live2DStudio',
        component: ()=>import("@/pages/Live2DStudio.vue"),
    }
]

const router = createRouter({
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