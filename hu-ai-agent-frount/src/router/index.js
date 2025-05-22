import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/love-app',
      name: 'love-app',
      component: () => import('../views/LoveAppView.vue')
    },
    {
      path: '/manus',
      name: 'manus',
      component: () => import('../views/ManusView.vue')
    }
  ]
})

export default router 