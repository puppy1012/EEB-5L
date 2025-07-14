import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

import GameChipList from "../pages/list/GameChipList.vue";
import GameChipRegister from "../pages/register/GameChipRegister.vue";

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        redirect: '/list'
    },
    {
        path: '/list',
        name: 'GameChipList',
        component: GameChipList,
    },
    {
        path: '/register',
        name: 'GameChipRegister',
        component: GameChipRegister,
    },
    // {
    //     path: '/read/:boardId',
    //     name: 'VueBoardRead',
    //     components: { default: VueBoardRead },
    //     props: { default: true },
    // },
    // {
    //     path: '/update/:boardId',
    //     name: 'VueBoardUpdate',
    //     components: { default: VueBoardUpdate },
    //     props: { default: true },
    // },
]

const router = createRouter({
    history: createWebHistory('/game-chip/'),
    routes,
})

export default router