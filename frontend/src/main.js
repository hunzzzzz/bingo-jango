import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(router)
app.mount('#app')

router.afterEach((to) => {
    document.title = to.meta?.title
        ? `${to.meta.title} — sparta-team`
        : 'sparta-team'
})