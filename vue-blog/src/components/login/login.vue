<template>
  <div class="login" ref="login" :style="{background:`url(https://www.luckyclover.top/myapi/user/getBg?t=${t})`}">
    <div class="content" @mousedown="move" ref="move">
      <div class="tab">
        <div :class="islogin?'active':''" @click="loginBtn">登录</div>
        <div :class="!islogin?'active':''" @click="registerBtn">注册</div>
      </div>
      <div class="loginbox" v-show="islogin" @mousedown.stop="">
        <div>
          <input placeholder="用户名 / 邮箱" type="text" list="user" class="textbox" v-model="user.userName">
          <datalist id="user">
            <option v-for="i in cache" :key="i" :value="i"></option>
          </datalist>
        </div>
        <div>
          <form action="">
            <input placeholder="密码" :type="passwordtype"  class="textbox" v-model="user.password" autocomplete="off" @keydown.enter.prevent.stop="login">
          </form>
          <svg class="icon" v-show="passwordtype=='password'" aria-hidden="true" @click="passwordtype=(passwordtype=='password')?'text':'password'">
            <use xlink:href="#icon-kejian"></use>
          </svg>
          <svg class="icon" v-show="passwordtype=='text'" aria-hidden="true" @click="passwordtype=(passwordtype=='password')?'text':'password'">
            <use xlink:href="#icon-yincang"></use>
          </svg>
        </div>
        <div>
          <input type="checkbox" style="width:20px;height:20px;margin-right:10px" v-model="auto">自动登录
          <span @click="login">
            登录
          </span>
        </div>
      </div>
      <div class="loginbox" v-show="!islogin" @mousedown.stop="">
        <div :class="NameForm">
          <input placeholder="用户名 (2-10个字符)" type="text"  class="textbox" v-model="user.userName" @change="namedone">
        </div>
        <div :class="EmailForm">
          <input placeholder="邮箱" type="text"  class="textbox" v-model="user.email" @change="done">
        </div>
        <div>
          <form action="">
            <input placeholder="密码 (6-20个字符)" :type="texttype"  class="textbox" v-model="user.password" autocomplete="off" @keydown.enter.prevent.stop="register">
          </form>
          <svg class="icon" v-show="texttype=='password'" aria-hidden="true" @click="texttype=(texttype=='password')?'text':'password'">
            <use xlink:href="#icon-kejian"></use>
          </svg>
          <svg class="icon" v-show="texttype=='text'" aria-hidden="true" @click="texttype=(texttype=='password')?'text':'password'">
            <use xlink:href="#icon-yincang"></use>
          </svg>
        </div>
        <div class="log">
          <input type="text" v-model="code" @keydown.enter.prevent.stop="register">
          <img :src="src" style="width:80px;height:32px;margin-right:20px" alt="验证码" @click="getImage" :key="key">
          <span style="width:auto" @click="register">
            注册
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {register, login, getCodeImage} from 'api/user'
import {saveUser, loadUser, saveToken, saveAuto} from 'common/js/cache'
import {mapMutations} from 'vuex'
export default {
  name: 'login',
  components: {},
  props: {},
  data () {
    return {
      user: {'userName': loadUser()[0]},
      islogin: true,
      passwordtype: 'password',
      texttype: 'password',
      code: '',
      src: '',
      key: '',
      EmailForm: '',
      NameForm: '',
      auto: false,
      cache: loadUser(),
      t: Math.random()
    }
  },
  watch: {},
  computed: {
    allRight () {
      if (this.user.userName.length >= 2 && this.user.userName.length <= 10 && this.user.password.length >= 6 && this.user.password.length <= 20 && this.EmailForm === '' && this.NameForm === '') return true
      else return false
    }
  },
  methods: {
    move (event) {
      let rect = this.$refs.move.getBoundingClientRect()
      let diffX = event.clientX - rect.left
      let diffY = event.clientY - rect.top
      let _this = this
      document.onmousemove = function (e) {
        _this.$refs.move.style.left = e.clientX - diffX + (rect.right - rect.left) / 2 + 'px'
        _this.$refs.move.style.top = e.clientY - diffY + 'px'
      }
      document.onmouseup = function (e) {
        document.onmousemove = null
        document.onmouseup = null
      }
    },
    loginBtn () {
      this.islogin = true
    },
    registerBtn () {
      this.islogin = false
    },
    getImage () {
      let _this = this
      getCodeImage().then((res) => {
        _this.src = 'data:image/jpeg;base64,' + res.data.image
        _this.key = res.data.key
      })
    },
    done () {
      let regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
      this.EmailForm = regEmail.test(this.user.email) ? '' : 'wrong'
    },
    namedone () {
      let name = this.user.userName
      let i = parseInt(name)
      this.NameForm = i + '' === name ? 'namewrong' : ''
    },
    register () {
      if (!this.allRight) {
        alert('请正确填写信息')
        return
      }
      register(this.code, this.key, this.user).then((res) => {
        if (res.data.state) {
          alert(res.data.msg + '\n您可以使用该ID登录博客')
          this.login()
        } else {
          alert(res.data.msg)
          this.getImage()
        }
      })
    },
    login () {
      login(this.user).then((res) => {
        if (res.data.state === true) {
          this.user = res.data.data
          this.setUser(this.user)
          this.setLogin(true)
          this.$router.push({
            path: '/' + this.user.id
          })
          saveUser(this.user.userName)
          saveToken(res.data.msg)
          saveAuto(this.auto)
        } else {
          alert(res.data.msg)
        }
      })
    },
    ...mapMutations({
      setLogin: 'SET_LOGIN',
      setUser: 'SET_USER'
    })
  },
  created () {
    this.getImage()
  },
  mounted () {
    this.islogin = !this.$route.params.register
  }
}
</script>
<style  lang="scss" scoped>
.login{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: black;
  background-position: center center;
  background-size: cover;
  .content {
    position: absolute;
    width: 25%;
    top: 25%;
    left: 50%;
    transform: translate(-50%,0);
    background-color: rgba($color: #fff, $alpha: 0.6);
    border-radius: 12px;
    overflow: hidden;
    min-width: 300px;
    box-shadow:0 0 30px 5px rgba(0, 0, 0, 0.36);
    .tab {
      border-bottom: 1px solid black;
      display: flex;
      align-items: center;
      .active {
        background-color: rgba($color: #fff, $alpha: 0.6);
      }
      div {
        height: 100%;
        text-align: center;
        flex: 1;
        font-size: 1.5rem;
        line-height: 150%;
        cursor: pointer;
        background-color: rgba($color: rgb(187, 187, 187), $alpha: 0.6);
        &:hover {
          background-color: rgba($color: #fff, $alpha: 0.8);
        }
        &:first-child {
          border-right: 1px solid black;
        }
      }
    }
    .loginbox {
      div {
        margin: 25px auto;
        width: 85%;
        font-size: 25px;
        position: relative;
        .textbox {
          background: rgba($color: #fff, $alpha: 1);;
          font-size: 25px;
          width: 100%;
          margin: 0 auto;
          border: none;
          outline:none;
          line-height: 40px;
          padding-left: 10px;
          border-radius: 5px;
        }
        .icon {
          user-select: none;
          position: absolute;
          bottom: 0;
          right: 0;
        }
        .textbox:focus {
          background-color: #fff;
        }
        .textbox::-webkit-input-placeholder {
          color: rgba(83, 194, 116, 0.6);
        }
        span {
          float: right;
          width: 45%;
          text-align: center;
          border-radius: 10px;
          color: white;
          background-color: rgba(26, 109, 218, 0.6);
          &:hover {
            background-color: rgba(26, 109, 218, 0.9);
          }
        }
      }
      .log {
        height: 40px;
        display: flex;
        align-items: center;
        input {
          border: none;
          outline:none;
          width: 80px;
          height: 30px;
          font-size: 20px;
          margin: 0;
          margin-right: 20px;
        }
        span {
          flex: 1;
        }
      }
      .namewrong {
        .textbox {
          border: red solid 1px;
          margin-top: -1px;
          margin-bottom: -1px;
          background-color: rgb(248, 222, 222);
        }
      }
      .namewrong:after{
        content: "用户名不能是纯数字";
        position: absolute;
        font-size: 16px;
        color: red;
      }
      .wrong {
        .textbox {
          border: red solid 1px;
          margin-top: -1px;
          margin-bottom: -1px;
          background-color: rgb(248, 222, 222);
        }
      }
      .wrong:after{
        content: "请输入正确的邮箱格式";
        position: absolute;
        font-size: 16px;
        color: red;
      }
    }
  }
}
</style>
