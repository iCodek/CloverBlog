<template>
  <div class="user-info">
    <div class="userbg"></div>
    <div class="userheadwrap">
      <div class="userhead" :style="{backgroundImage: user.avatar?`url(${user.avatar})`:``}"></div>
    </div>
    <div class="username">{{user.userName}}</div>
    <div class="sum">
      <div class="count" v-for="(key,i) in [user.blogCount, user.views, user.comments]" :key="i">
        <div class="wrap">
          {{toChinese[i]}}
        </div>
        <div class="wrap">
          {{key}}
        </div>
      </div>
    </div>
    <div class="contacts">
      <span data-title="邮箱" v-if="user.contact && user.contact.email">
        <svg class="icon" aria-hidden="true">
          <a :href="`mailto:${user.contact.email}`">
            <use xlink:href="#icon-youxiang1"></use>
          </a>
        </svg>
      </span>
      <span data-title="GitHub" v-if="user.contact && user.contact.github">
        <svg class="icon" aria-hidden="true">
          <a :href="user.contact.github">
            <use xlink:href="#icon-githubb"></use>
          </a>
        </svg>
      </span>
      <span data-title="QQ" v-if="user.contact && user.contact.qq">
        <svg class="icon" aria-hidden="true">
          <a href="javascript:;" @click="copyText(user.contact.qq)">
            <use xlink:href="#icon-QQ1"></use>
          </a>
        </svg>
      </span>
      <span data-title="知乎" v-if="user.contact && user.contact.zhihu">
        <svg class="icon" aria-hidden="true">
          <a :href="user.contact.zhihu">
            <use xlink:href="#icon-zhihu1"></use>
          </a>
        </svg>
      </span>
      <span data-title="B站" v-if="user.contact && user.contact.bili">
        <svg class="icon" aria-hidden="true">
          <a :href="user.contact.bili">
            <use xlink:href="#icon-Bilibili"></use>
          </a>
        </svg>
      </span>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
  components: {},
  props: {
  },
  data () {
    return {
      // user: {
      //   pic: null,
      //   name: '四叶草',
      //   count: {
      //     paperCount: 100,
      //     visit: 10,
      //     comment: 40
      //   },
      //   contact: {
      //     email: '790477428@qq.com',
      //     qq: '790477428',
      //     github: 'https://github.com/iCodek/vue-blog',
      //     zhihu: 1,
      //     bili: 0
      //   }
      // },
      toChinese: [
        '文章',
        '浏览',
        '评论'
      ]
    }
  },
  watch: {},
  computed: {
    ...mapGetters([
      'user',
      'index'
    ])
  },
  methods: {},
  created () {},
  mounted () {}
}
</script>
<style lang="scss" scoped>
.user-info{
  text-align: center;
  background-color: var(--color);
  margin-bottom: 20px;
  border-radius: 9px;
  .userbg {
    border-top-right-radius: 9px;
    border-top-left-radius: 9px;
    height: 120px;
    background: url('../../assets/userbg.jpg') 70% center no-repeat;
    opacity: var(--opacity);
  }
  .userheadwrap {
    position: relative;
    .userhead {
      left: 50%;
      position: absolute;
      width: 90px;
      height: 90px;
      border-radius: 50%;
      box-shadow:0 0 30px 10px rgba(148, 146, 146, 0.36);
      // background-color: red;
      background-image: url('../../assets/siyecao1.jpg');
      background-position: center center;
      background-size: cover;
      transition: all 0.4s;
      transform: translate(-50%, -50%);
      &:hover {
        width: 120px;
        height: 120px;
        transform: translate(-50%, -50%) rotate(360deg);
      }
    }
  }
  .username {
    line-height: 32px;
    font-size: 28px;
    margin: 66px auto 15px;
  }
  .sum {
    width: 100%;
    height: 50px;
    display: flex;
    font-size: 20px;
    user-select: none;
    .count {
      flex: 1;
      display: flex;
      flex-direction: column;
      .wrap {
        flex: 1;
      }
    }
  }
  .contacts {
    margin: 24px auto 0 auto;
    display: flex;
    width: 92%;
    span {
      flex: 1;
      position: relative;
      width:  10px;
      margin-bottom: 18px;
      .icon {
        font-size: 45px;
      }
      &:after {
        content: attr(data-title);    //取到data-title属性的值
        background:#000;
        width: 80px;
        line-height: 35px;
        border-radius: 10px;
        font-size: 16px;
        color: #fff;
        position: absolute;
        bottom: 58px;
        left: 50%;
        transform: translateX(-50%);
        transition: all 0.4s;
        height: 0px;
        opacity: 0;
      }
      &:hover::after {
        opacity: 1;
        height: 35px;
      }
      &:before {
        content: "";
        position: absolute;
        bottom: 50px;
        left: 50%;
        overflow: hidden;
        transform: translateX(-50%);
        border-top: 8px solid #000;
        border-left: 6px solid transparent;
        border-right: 6px solid transparent;
        transition: all 0.4s;
        opacity: 0;
      }
      &:hover::before{
        opacity: 1;
      }
    }
  }
}
</style>
