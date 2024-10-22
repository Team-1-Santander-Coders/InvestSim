import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from './stores/auth'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('./views/HomeView.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/user',
        name: 'User Dashboard',
        component: () => import('./views/User.vue'),
        meta: { requiresAuth: true }
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    if (to.meta.requiresAuth && !authStore.isAuthenticated) {
        next('/')
    } else if ((to.path === '/login' || to.path === '/register') && authStore.isAuthenticated) {
        next('/user')
    } else {
        next()
    }
})

export default router