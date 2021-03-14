<template>
  <div class="search">
    <transition name="fade">
      <div class="search-box" v-show="show" ref="searchBox">
        <!-- <input type="text" @focus="addBorder()" @blur="delBorder()" placeholder="搜索其他用户ID"> -->
        <input type="text" placeholder="搜索博客标题，摘要，标签，内容" v-model="filter">
        <div v-show="empty" style="position: absolute; top: 40px;background:var(--color);width:92%;z-index:2000;padding:20px 0 20px 0">
          <img src="../../assets/no-data.png">
        </div>
        <transition name="fade-out">
          <ul :class="cls">
            <li v-for="blog in searchList" :key="blog.blogId" @click="go(blog.userId, blog.blogId)">
              <div class="wrap">
                <div class="content">
                  <span class="title">标题：</span><span v-html="blog.title"></span>
                  <span style="float:right;">
                    <span class="title">标签：</span><span v-html="trim(blog.tags)"></span>
                  </span>

                </div>
                <div class="content"><span class="title">摘要：</span><span v-html="blog.preview"></span></div>
                <div class="content"><span class="title">正文：</span><span v-html="blog.content"></span></div>
              </div>
            </li>
          </ul>
        </transition>
      </div>
    </transition>
    <svg class="icon" aria-hidden="true" @click='toggleSearchBox()' @mouseenter="showSearchBox()">
      <use xlink:href="#icon-sousuo"></use>
    </svg>
  </div>
</template>

<script>
import {debounce} from 'common/js/util'
// import {saveSearch, loadSearch} from 'common/js/cache'
import {mapGetters} from 'vuex'
import {getBlogSearch} from 'api/es'

export default {
  components: {},
  props: {},
  data () {
    return {
      show: false,
      searchList: [],
      filter: '',
      empty: false
    }
  },
  watch: {
    show (value) {
      if (!value) {
        this.delAll()
      }
    }
  },
  computed: {
    cls () {
      return this.searchList.length && this.show ? 'list' : 'list hide'
    },
    ...mapGetters([
      'user'
    ])
  },
  methods: {
    trim (value) {
      return value.substr(1, value.length - 2)
    },
    toggleSearchBox () {
      this.searchList = []
      this.show = !this.show
    },
    showSearchBox () {
      this.show = true
    },
    delAll () {
      this.searchList = []
      this.filter = ''
      this.empty = false
    },
    go (userId, blogId) {
      if (this.$route.name === 'index') this.$router.push({name: 'blog', params: {id: userId, blogid: blogId}})
      else this.$router.push({path: '/:id'}).then(() => this.$router.push({name: 'blog', params: {id: userId, blogid: blogId}}))
    }
  },
  created () {},
  mounted () {
    this.$watch('filter', debounce((newQuery) => {
      if (newQuery) {
        this.empty = false
        getBlogSearch(this.user.id, newQuery).then((res) => {
          if (res.data.state) {
            this.searchList = res.data.data
            if (this.searchList.length === 0) this.empty = true
          } else {
            alert(res.data.msg)
          }
        })
      }
    }, 1000))
  }
}
</script>
<style  lang="scss" scoped>
.search{
  position: absolute;
  bottom: 8px;
  right: 16px;
  z-index: 1600;
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
  margin-right: 42px;
  height: 40px;
  width:25%;
  min-width: 480px;
  background-color: rgba($color: #caf0cf, $alpha: 0.8);
  text-align: center;
  display:flex;
  box-sizing: border-box;
  position: static;
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
      left: 0;
      list-style: none;
      background-color: var(--color);
      width: calc(89% + 15px);
      border-radius: 8px;
      max-height: 800px;
      overflow: auto;
      transition: all 0.5s;
      box-shadow:0px 20px 30px 10px rgba(0, 0, 0, 0.36);
      // &::-webkit-scrollbar-track {
      //   box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
      //   border-radius: 10px;
      //   background: #ededed;
      // }
      li {
        text-align: left;
        padding: 6px 4px 6px 8px;
        border-bottom: 2px black solid;
        &:hover {
          background-color: rgb(231, 231, 231);
        }
        .wrap {
          font-size: 14px;
          line-height: 18px;
          display: flex;
          flex-direction: column;
          .content {
            .title {
              color: rgb(47, 144, 255);
            }
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
    right: 0;
    transition: border-radius 0.5s ease;
  }
  :hover{
    border-radius: 8px;
  }
}
</style>
