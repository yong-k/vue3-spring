<script setup>
import router from '@/router/router'
import axios from 'axios'
import { ref, reactive } from 'vue'

const passwordCheck = ref("")
const usernameErrMsg = ref([])
const emailErrMsg = ref([])
const passwordErrMsg = ref([])
let usernameFlag = false
let emailFlag = false
let passwordFlag = false

const form = reactive({
  name: "",
  username: "",
  email: "",
  password: "",
  address: "",
  phone: "",
  website: "",
  company: ""
})

const rules = {
  required: value => !!value || '필수 항목입니다.',
  username: value => checkUsername(value),
  email: {
    format: value => {
      const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      return pattern.test(value) || '이메일 형식을 확인해주세요.'
    },
    duplicate: value => checkEmail("", value)
  },
  password: value => checkPassword(value, passwordCheck.value),
  passwordCheck: value => checkPassword(form.password, value)
}

const checkValid = () => {
  if (!form.username || !form.email || !form.password || !passwordCheck.value)
    return false

  if (usernameFlag && emailFlag && passwordFlag) 
    submit()

  return false
}

function checkUsername(username) {
  const params = { username: username }
  axios.get("/api/checkusername", {params})
  .then((res) => {
    usernameFlag = res.data.true
    if (usernameFlag) 
      usernameErrMsg.value = []
    else
      usernameErrMsg.value = '사용할 수 없는 아이디입니다.'   
    return usernameFlag
  })
  .catch(err => {
    console.log(err)
    window.alert('예상치 못한 오류가 발생했습니다.');
  })
}

function checkEmail(username, email) {
  const params = {
    username: username,
    email: email
  }
  axios.get("/api/checkemail", {params})
    .then((res) => {
      emailFlag = res.data.true
      if (emailFlag)
        emailErrMsg.value = []
      else
        emailErrMsg.value = ['사용할 수 없는 이메일입니다.']
      return emailFlag
    })
    .catch(err => {
      console.log(err)
      window.alert('예상치 못한 오류가 발생했습니다.');
    })
}

function checkPassword(pw, checkPw) {
  if (pw === checkPw) {
    passwordErrMsg.value = []
    passwordFlag = true
  } else {
    passwordErrMsg.value = ['비밀번호가 일치하지 않습니다.']
    passwordFlag = false
  }
  return passwordFlag
}

function submit() {
  axios.post("/api/user", form)
    .then(res => {
      if (res.data.code === 0) 
        router.push({ path: "/user/list" })
      else 
        window.alert('오류가 발생했습니다. 다시 시도해주세요.')
    })
    .catch(err => {
      console.log(err)
      window.alert('예상치 못한 오류가 발생했습니다.')
    })
}
</script>

<template>
  <div class="container">
    <h3 class="page-title">회원 등록</h3>
    <v-sheet width="400" class="mx-auto">
      <v-form>
        <v-text-field label="name" v-model="form.name" />
        <v-text-field label="username" v-model="form.username" :rules="[rules.required, rules.username]" 
        :error-messages="usernameErrMsg" />
        <v-text-field label="email" v-model="form.email" :rules="[rules.required, rules.email.format, rules.email.duplicate]"
        :error-messages="emailErrMsg" />
        <v-text-field label="password" type="password" v-model="form.password" :rules="[rules.required, rules.password]" 
        :error-messages="passwordErrMsg" />
        <v-text-field label="password-check" type="password" v-model="passwordCheck" :rules="[rules.required, rules.passwordCheck]" 
        :error-messages="passwordErrMsg"/>
        <v-text-field label="address" v-model="form.address" />
        <v-text-field label="phone" v-model="form.phone" />
        <v-text-field label="website" v-model="form.website" />
        <v-text-field label="company" v-model="form.company" />
        <v-btn @click="checkValid()" block class="mt-2" color="#5865f2" size="large">등록</v-btn>
      </v-form>
    </v-sheet>
  </div>
</template>

<style scoped>
</style>
