<script setup>
import router from '@/router/router';
import { useAuthStore } from '@/stores/auth'
import axios from 'axios';

const authStore = useAuthStore()

function logout() {
    axios.post("/api/logout")
        .then(res => {
            router.push({ path: "/login" })
        })
        .catch(err => {
            console.log(err)
            window.alert('예상치 못한 오류가 발생했습니다.')
        })
}
</script>

<template>
    <div id="header">
        <router-link to="/" v-if="!authStore.username">
            <img id="logo" src="@/assets/vue-home.png">
        </router-link>
        <router-link to="/user/list" v-else>
            <img id="logo" src="@/assets/vue-home.png">
        </router-link>
        <div class="header-right-box">
            <span id="username" v-if="authStore.username">{{ authStore.username }} 님</span>
            <router-link to="/login" id="login" v-if="!authStore.username">로그인</router-link>
            <a id="logout" @click="logout()" v-else>로그아웃</a>
        </div>
    </div>
</template>

<style scoped>
#logo {
  width: 5%;
  cursor: pointer;
}

.header-right-box {
    float: right;
}

.header-right-box #username {
    margin-right: 15px;
}

.header-right-box #login {
    text-decoration: none;
    color: #000000;
}

.header-right-box #logout {
    cursor: pointer;
}
</style>