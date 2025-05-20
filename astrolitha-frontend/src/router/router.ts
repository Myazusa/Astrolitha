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
        children: [
            {
              path: '',
              name: 'UserCenterProfile',
              component: () => import("@/components/user-center/UserCenterProfile.vue")
            },
            {
                path: 'chat',
                name: 'UserCenterChat',
                component: () => import("@/components/user-center/UserCenterChat.vue"),
            },
            {
                path: 'option',
                name: 'UserCenterOption',
                component: () => import("@/components/user-center/UserCenterOption.vue"),
            }
        ]
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