<template>
  <div class="search">
    <svg class="icon" aria-hidden="true" @click='toggleSearchBox()' @mouseenter="showSearchBox()">
      <use xlink:href="#icon-haoyou"></use>
    </svg>
    <transition name="fade">
      <div class="search-box" v-show="show" ref="searchBox">
        <!-- <input type="text" @focus="addBorder()" @blur="delBorder()" placeholder="搜索其他用户ID"> -->
        <input type="text" @focus="showAll" placeholder="搜索其他用户ID" v-model="filter">
        <transition name="fade-out">
          <ul :class="cls">
            <li v-for="user in userList" :key="user.id" @click="go(user.id)" v-show="user.id.includes(filter)">
              <div class="wrap">
                <div class="id">{{user.id}}</div>
                <div class="name">{{user.userName}}</div>
                <div class="count">{{user.blogCount}}</div>
                <div class="avatar" :style="{backgroundImage:`url(${user.avatar})`}"></div>
              </div>
            </li>
          </ul>
        </transition>
      </div>
    </transition>
  </div>
</template>

<script>
import {getAllUser} from 'api/user'
export default {
  components: {},
  props: {},
  data () {
    return {
      show: false,
      userList: [],
      filter: ''
    }
  },
  watch: {},
  computed: {
    cls () {
      return this.userList.length && this.show ? 'list' : 'list hide'
    }
  },
  methods: {
    toggleSearchBox () {
      this.userList = []
      this.show = !this.show
    },
    showSearchBox () {
      this.show = true
    },
    delAll () {
      this.userList = []
    },
    showAll () {
      if (this.userList.length) return
      getAllUser().then((res) => {
        this.userList = res.data.data
      })
    },
    go (id) {
      window.open('https://www.luckyclover.top/' + id)
    }
    // addBorder () {
    //   this.$refs.searchBox.style.border = '2px solid green'
    // },
    // delBorder () {
    //   this.$refs.searchBox.style.border = ''
    // }
  },
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.search{
  position: absolute;
  bottom: 8px;
  left: 8px;
  z-index: 1200;
  .fade-enter-active {
    transition: all .4s ease;
  }
  .fade-leave-active {
    transition: all .4s cubic-bezier(1.0, 0.5, 0.8, 1.0);
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
    opacity: 0;
  }
  .search-box{
  border-radius: 8px;
  margin-left: 42px;
  height: 40px;
  width:25%;
  min-width: 280px;
  background-color: rgba($color: #caf0cf, $alpha: 0.8);
  text-align: center;
  display:flex;
  box-sizing: border-box;
    input {
      margin: auto;
      width: 94%;
      height: 70%;
      border: none;
      outline:none;
      padding-left: 5px;
      transition: all 0.2s;
    }
    .hide {
      max-height: 0 !important;
    }
    .list {
      position: absolute;
      top: 38px;
      right: 0;
      list-style: none;
      background-color: var(--color);
      width: calc(82% + 15px);
      border-radius: 8px;
      max-height: 600px;
      overflow: auto;
      transition: all 0.5s;
      box-shadow:0px 20px 30px 10px rgba(0, 0, 0, 0.36);
      // &::-webkit-scrollbar-track {
      //   box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
      //   border-radius: 10px;
      //   background: #ededed;
      // }
      li {
        height: 50px;
        &:hover {
          background-color: rgb(231, 231, 231);
        }
      }
      .wrap {
        display: flex;
        width: 100%;
        align-items: center;
        line-height: 50px;
        position: relative;
        .id {
          width: 50px;
        }
        .name {
          text-align: left;
          flex: 1;
        }
        .count {
          position: absolute;
          right: 85px;
        }
        .avatar {
          position: absolute;
          right: 20px;
          border-radius: 50%;
          width: 40px;
          height: 40px;
          background-size: cover;
          transition: all 0.2s;
          &:hover {
            z-index: 1500;
            transform: scale(2);
          }
        }
      }
    }
  }
  .icon{
    background-color:whitesmoke;
    bottom: 0;
    border-radius: 50%;
    position: absolute;
    font-size: 36px;
    left: 0;
    transition: border-radius 0.5s ease;
  }
  :hover{
    border-radius: 8px;
  }
}
</style>
