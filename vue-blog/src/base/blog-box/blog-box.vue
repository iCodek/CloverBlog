<template>
  <router-link tag="div" class="outer" :to="{name: 'blog', params: {id: blog.userId, blogid: blog.blogId }}" v-if="blog.visible" v-show="(category===''||blog.category===category)&&(isOfTags(blog.tags))"
   :style="{width : boxWidth + '%'}">
    <transition name="fade-in">
      <div class="blog-box" v-if="blogBox" key="0">
        <!-- https://random.52ecy.cn/randbg.php -->
        <div class="pic"  v-lazy:background-image="{src:user.api ? `${user.api}?t=${blog.blogId}` : `https://www.luckyclover.top/myapi/blog/getImage?t=${blog.blogId}`}"></div>
        <div class="mask" :style="{backgroundColor: `rgba(0,0,0,${(120 - boxWidth) / 400})`}"></div>
        <div class="title" :style="{fontSize : (boxWidth - 21) / 80 * 2 + 1.4 + 'vw', lineHeight: (boxWidth - 21) / 80 * 2 + 1.5 + 'vw'}">{{blog.title}}</div>
        <div class="foot">
          <div class="description" :style="{fontSize : (boxWidth - 21) / 80 * 2 + 0.6 + 'vw', lineHeight: 1.1}">{{blog.preview}}</div>
          <div class="line" v-show="boxWidth > 32"></div>
          <div class="sum" v-show="boxWidth > 32">
            <div class="time">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-shijian"></use>
              </svg>
              {{blog.uploadTime}}
            </div>
            <div class="count" v-show="boxWidth > 49">
              <div>
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-liulan2"></use>
                </svg>
                {{blog.views}}
              </div>
              <div>
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-pinglun2"></use>
                </svg>
                {{blog.comments}}
              </div>
              <div>
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-dianzan1"></use>
                </svg>
                {{blog.likes}}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="list" key="1">
        <div style="width:36px;margin-left:5px;">{{index+1}}</div>
        <div class="list-title" :style="{fontSize : (boxWidth - 21) / 80 * 0.8 + 1 + 'vw', lineHeight: 1.1}">{{blog.title}}</div>
        <div class="list-desc" v-show="boxWidth > 32">{{blog.preview}}</div>
        <div class="list-uptm" v-show="boxWidth > 49">{{blog.uploadTime.substring(0,blog.uploadTime.length-3)}}</div>
      </div>
    </transition>
  </router-link>
</template>

<script>
// https://api.imo6.cn/nice/api.php
// https://api.imo6.cn/anime720/api.php
import {mapGetters} from 'vuex'

export default {
  components: {},
  props: {
    blog: {
      type: Object,
      default () {
        return {}
      }
    },
    index: {
      type: Number,
      default () {
        return 0
      }
    }
  },
  data () {
    return {}
  },
  watch: {},
  computed: {
    ...mapGetters([
      'user',
      'category',
      'tagsOn',
      'blogBox',
      'boxWidth'
    ])
  },
  methods: {
    isOfTags (tags) {
      for (let tagOn of this.tagsOn) {
        if (!tags.includes(tagOn)) {
          return false
        }
      }
      return true
    }
  },
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.outer {
  min-height: 60px;
  position: relative;
  margin: 5px;
  margin-bottom: 8px;
  @media screen and (max-width:1200px){
    width: 100% !important;
    margin: 0px;
  }
}
.blog-box{
  box-shadow: 1px 1px 3px 1px rgba(0,0,0,.2);
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  transition: box-shadow 0.4s ease;
  position: relative;
  width: 100%;
  padding-top: 40%;
  // height: 200px;
  margin-bottom: 3%;
  overflow: hidden;
  .pic {
    top: 0;
    position: absolute;
    width: 100%;
    height: 100%;
    // background: url(https://random.52ecy.cn/randbg.php);
    background-position: center 36%;
    background-size: cover;
    opacity: calc(var(--opacity) * 2);
    transition: transform 0.2s ease, filter 0.6s ease;
  }
  .mask {
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    transition: background-color 0.6s ease;
    opacity: var(--opacity);
  }
  .title {
    width: 96%;
    position: absolute;
    transition: all 0.2s ease;
    top: 40%;
    left: 50%;
    color: #fff;
    text-shadow: 0 0 5px #000;
    transform: translate(-50%, -50%);
    @media screen and (max-width:1200px){
      font-size: 6vw!important;
    }
  }
  .foot {
    position: absolute;
    bottom: 10%;
    height:30%;
    width: 100%;
    opacity: 0;
    transition: all 0.2s ease;
    font-size: 1.3rem;
    display: flex;
    flex-direction: column;
    @media screen and (max-width:1200px){
      font-size: 2vw ;
    }
    @media screen and (max-width:1600px) and (min-width:1200px){
      font-size: 1.2vw;
    }
    @media screen and (min-width:2000px){
      font-size: 1.8vw;
    }
    .description {
      flex: 1;
      top: 0;
      margin: 0 1vw;
      color: #fff;
      text-align: left;
      overflow: hidden;
      @media screen and (max-width:1200px){
        font-size: 2vw !important;
      }
      //text-indent: 4rem;
      //font-size: 2vw;
    }
    .line {
      width: 96%;
      height: 1px;
      margin: 0.2rem auto;
      background-color: rgba($color: #ffffff, $alpha: 0.5);;
    }
    .sum {
      color: #fff;
      width: 96%;
      margin: 0.3rem auto;
      margin-top: 0;
      display: flex;
      .icon {
        font-size: 1.3rem;
        margin-bottom: 0.15rem;
        @media screen and (max-width:1200px){
          font-size: 2vw;
        }
        @media screen and (max-width:1600px) and (min-width:1200px){
          font-size: 1.2vw;
        }
        @media screen and (min-width:2000px){
          font-size: 1.8vw;
        }
      }
      .time {
        text-align: left;
        flex: 1;
      }
      .count {
        div {
          display: inline-block;
          margin-left: 1vw;
        }
      }
    }
  }
  &:hover > .pic {
    transform: scale(1.2);
    filter: blur(2px);
    -webkit-filter: blur(2px);
  }
  &:hover {
    box-shadow: 3px 3px 12px 3px rgba(0,0,0,.2)!important;
  }
  &:hover > .mask{
    background-color: rgba($color: #4d4c4c, $alpha: 0.5);
  }
  &:hover > .title{
    text-shadow: 0 0 2px #fff;
    top: 25%;
  }
  &:hover > .foot{
    bottom: 0;
    height:50%;
    opacity: 1;
  }
}
.list {
  position: absolute;
  top: 0;
  background-color: var(--color);
  width: 100%;
  height: 60px;
  font-size: 1.6rem;
  display: flex;
  align-items: center;
  border-radius: 9px;
  opacity: 0.95;
  &:hover {
    background-color: rgb(231, 231, 231);
  }
  .list-title {
    flex: 1;
    text-align: left;
  }
  .list-desc {
    text-align: left;
    width: 35%;
    font-size: 16px;
    max-height: 60px;
    overflow: hidden;
    text-overflow:ellipsis;
  }
  .list-uptm {
    margin-right: 10px;
    font-size: 20px;
  }
}
.fade-in-enter-active {
  transition: all 1.2s ease;
}
.fade-in-leave-active {
  transition: all 1.2s ease-out;
}
.fade-in-enter, .fade-in-leave-to {
  padding-top: 0;
  opacity: 0;
  margin-bottom: 0;
}
</style>
