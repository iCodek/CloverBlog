<template>
  <div class="tab">
    <div class="index" @click="setCategory(''),push()">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-fangzi"></use>
      </svg>
      <div class="shouye">首页</div>
    </div>
    <div class="line"></div>
    <div class="titles">
    <!-- <transition-group name="cell" tag="div" class="titles"> -->
      <div :class="cls(title.name)" v-for="(title,index) in titles" :key="title.name" @click="setCategory(title.name),push()">{{title.name}}
        <span class="count" :style="{backgroundColor : color[index]}">{{title.count}}</span>
      </div>
    <!-- </transition-group> -->
    </div>
    <div class="uploadsvg">
      <!-- <span title="在线编辑">
        <svg class="icon" aria-hidden="true" @click="edit">
          <use xlink:href="#icon-shuxie"></use>
        </svg>
      </span> -->
      <span title="本地上传" @click="upbtn">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-shangchuan1-copy"></use>
        </svg>
      </span>
      <span title="切换显示（试试滚轮吧）" @click="listbtn" :style="{transform: `rotate(${angle*45}deg)`}" @mousewheel.stop.prevent="wheel($event)">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-siyecaox"></use>
        </svg>
      </span>
    </div>
    <transition name="fade-out">
      <upload v-if="up" @back="up=false"></upload>
    </transition>
  </div>
</template>

<script>
// import _ from 'lodash/lodash'
import {rainbowColor} from 'common/js/util'
import upload from 'cpnts/upload/upload'
import {mapGetters, mapMutations} from 'vuex'
import {loadWidth, saveWidth} from 'common/js/cache'
export default {
  components: {
    upload
  },
  props: {},
  data () {
    return {
      titles: [],
      color: [],
      up: false,
      angle: 1
    }
  },
  watch: {
    blogs (newVal) {
      let objs = newVal.reduce((obj, item) => {
        if (!item.visible) return obj
        if (item.category in obj) obj[item.category]++
        else obj[item.category] = 1
        return obj
      }, {})
      let titles = []
      for (let title in objs) {
        titles.push({'name': title, 'count': objs[title]})
      }
      this.titles = titles.sort((a, b) => b.count - a.count)
      this.rainBow()
    }
  },
  computed: {
    ...mapGetters([
      'blogs',
      'category',
      'isLogin',
      'user',
      'blogBox',
      'boxWidth'
    ])
  },
  methods: {
    push () {
      if (this.$route.name !== 'index') {
        this.$router.push({
          path: '/' + this.user.id
        })
      }
    },
    upbtn () {
      if (this.isLogin) {
        this.up = true
      } else {
        alert('请先登录')
      }
    },
    listbtn () {
      this.angle += 1
      this.setList(!this.blogBox)
    },
    cls (t) {
      return t === this.category ? 'titlesbox on' : 'titlesbox'
    },
    rainBow () {
      this.color = rainbowColor(this.titles.length, 0, 225)
    },
    // shuffle () {
    //   this.titles = _.shuffle(this.titles)
    // },
    edit () {
      alert('暂时没打算开放在线编辑功能,觉得typora本地编写上传体验更好(附带上传md里引用的图片)。想开放在线编辑功能的同学可以去github留言')
    },
    wheel (e) {
      let width = this.boxWidth
      if (e.deltaY > 0) {
        width -= 1
        if (width < 21) width = 21
      }
      if (e.deltaY < 0) {
        width += 1
        if (width > 100) width = 100
      }
      this.setBoxWidth(width)
      saveWidth(width)
    },
    ...mapMutations({
      setCategory: 'SET_CATEGORY',
      setList: 'SET_BLOGBOX',
      setBoxWidth: 'SET_BOXWIDTH'
    })
  },
  created () {
    this.setBoxWidth(loadWidth())
  },
  mounted () {}
}
</script>
<style lang="scss" scoped>
.tab{
  border-radius: 10px;
  background-color: var(--color);
  margin-bottom: 15px;
  display: flex;
  user-select: none;
  .index {
    margin: 4px 5px 4px 10px;
    @media screen and (max-width:500px){
      margin: 0;
    }
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    svg {
      font-size: 3.2rem;
    }
    .shouye {
      font-size: 1rem;
      @media screen and (max-width:1200px){
        display: none;
      }
    }
    &:hover {
      color: #73c9e5;
    }
  }
  .line {
    height: auto;
    border-left: 1px solid #e7e7e7;
    margin: 10px 15px 10px 5px;
  }
  .titles {
    display: flex;
    flex-wrap: wrap;
    flex: 1;
    align-items: center;
    text-align: center;
    justify-content: flex-start;
    .titlesbox {
      z-index: 1;
      margin-right: 1rem;
      display: inline-block;
      font-size: 1.3rem;
      white-space: nowrap;
      // padding: 2px;
      box-sizing: border-box;
      // border: 2px solid black;
      position: relative;
      @media screen and (max-width:1200px){
        font-size: 3.6vw;
      }
      cursor: pointer;
      &:hover {
        color: #73c9e5;
      }
      .count {
        display: inline-block;
        border-radius: 100%;
        width: 2.4vw;
        color: rgb(255, 255, 255);
        opacity: 0.7;
        @media screen and (max-width:1200px){
          width: 6vw;
        }
      }
      &:hover > .count{
      opacity: 1;
      }
    }
    .on::before {
      z-index: -1;
      content: "";
      position: absolute;
      left: -2px;
      top: -2px;
      width: calc(100% + 4px);
      height: calc(100% + 4px);
      border: 1px solid rgba(0, 0, 0, 0.2);
      background-color: rgba(236, 113, 230, 0.2);
      border-radius: 5px;
    }
  }
  .uploadsvg {
    min-width: 110px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    span {
      cursor: pointer;
      transition: all .4s;
      &:first-child {
        font-size: 36px;
      }
      &:last-child {
        svg {
          font-size: 48px;
        }
      }
    }
    .rota0 {
      transform: rotate(0);
    }
    .rota45 {
      transform: rotate(45deg);
    }
    .rota90 {
      transform: rotate(90deg);
    }

    @media screen and (max-width:1200px){
      display: none;
    }
  }
  .fade-out-enter-active, .fade-out-leave-active {
    transition: opacity .4s;
  }
  .fade-out-enter, .fade-out-leave-to{
    opacity: 0;
  }
}
</style>
