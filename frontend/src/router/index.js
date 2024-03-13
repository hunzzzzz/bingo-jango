import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/components/Login.vue'
import HelloWorld from '@/components/Main.vue'

const routes = [
    {
        meta: {
            title: 'Main'
        },
        path: '/',
        name: 'main',
        component: HelloWorld
    },
    {
        meta: {
            title: 'Login'
        },
        path: '/login',
        name: 'login',
        component: Login
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        return savedPosition || { top: 0 }
    }
})

export default router
