import {createRouter, createWebHistory, RouteRecordRaw} from "vue-router";

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        component: ()=>import("@/App.vue"),
        children:[
            {
                path: '/',
                name: 'Live2DStudio',
                component: ()=>import("@/pages/Live2DStudio.vue"),
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
                path: 'database',
                name: 'UserCenterDatabase',
                component: () => import("@/components/user-center/UserCenterDatabase.vue"),
            },
            {
                path: 'tools',
                name: 'UserCenterCustomTool',
                component: ()=>import("@/components/user-center/UserCenterCustomTool.vue"),
            },
            {
                path: 'option',
                name: 'UserCenterOption',
                component: () => import("@/components/user-center/UserCenterOption.vue"),
            }
        ]
    },
    {
        path:'/login',
        name: 'UserLogin',
        component: ()=>import("@/pages/UserLogin.vue"),
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