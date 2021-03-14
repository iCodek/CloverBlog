<template>
  <div id="app">
    <div class="login_wrap">
      <router-link tag="div" class="login" :to="{name:'login'}" v-show="!isLogin">
        <span>登录</span>
      </router-link>
      <router-link tag="div" class="login" :to="{name:'login', params: { register: true }}" v-show="!isLogin">
        <span>注册</span>
      </router-link>
      <div class="login"  v-if="isLogin" @click="set=!set">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-yonghu3"></use>
        </svg>
      </div>
      <div class="login" v-if="isLogin">
        <span>欢迎 {{user.userName}}</span>
      </div>
      <div class="login" v-if="isLogin" @click="quit">
        <span>退出</span>
      </div>
      <div class="login">
        <a href="https://www.luckyclover.top/">首页</a>
      </div>
    </div>
    <transition name="fade">
      <!-- <keep-alive exclude="login"> -->
      <router-view></router-view>
      <!-- </keep-alive> -->
    </transition>
    <transition name="set">
      <setting v-if="set" @back="set=false"></setting>
    </transition>
    <textarea style="position:absolute;opacity:0" id="copyText"></textarea>
    <!-- <up></up> -->
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import setting from 'cpnts/setting/setting'
import up from 'cpnts/up/up'
import {visit} from 'api/user'
import {saveToken, saveAuto} from 'common/js/cache'
export default {
  name: 'App',
  data () {
    return {
      set: false
    }
  },
  components: {
    setting,
    up
  },
  watch: {},
  computed: {
    ...mapGetters([
      'user',
      'isLogin'
    ])
  },
  methods: {
    quit () {
      saveToken('')
      saveAuto(false)
      window.location.href = 'https://www.luckyclover.top/'
    }
  },
  created () {
    visit()
  }
}
</script>

<style lang="scss">
@import url("/common/css/index.css");
#app {
  position: relative;
  top: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  .login_wrap {
    position: fixed;
    right: 0;
    top: 0;
    z-index: 100;
    color: white;
    display: flex;
    flex-direction: row;
    user-select: none;
    .login {
      margin: 8px;
      font-size: 1.2rem;
      opacity: 0.8;
      cursor: pointer;
      svg {
        margin-top: -6px;
      }
      a {
        text-decoration: none;
        color: white;
      }
      &:hover {
        opacity: 1;
      }
      &:last-child {
        margin-right: 20px;
      }
    }
  }
  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to {
    opacity: 0;
  }
  .set-enter-active, .set-leave-active {
    transition: transform 0.5s ,opacity 0.8s ;
  }
  .set-enter, .set-leave-to {
    transform: translate(40%,-45%) scale(0.1);
    opacity: 0;
  }
}
</style>
