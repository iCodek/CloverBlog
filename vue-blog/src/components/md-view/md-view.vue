<template>
  <div class="md-view">
    <div class="title">
      <div class="back" @click="back">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#icon-siyecaox"></use>
        </svg>
      </div>
      <div class="like">
        <svg class="icon" aria-hidden="true" @click="tolike" v-if="!like">
          <use xlink:href="#icon-dianzan3"></use>
        </svg>
        <svg class="icon" aria-hidden="true" @click="tounlike" v-if="like">
          <use xlink:href="#icon-dianzan11"></use>
        </svg>
      </div>
      <div class="text">{{blog.title}}</div>
      <div class="svgs">
          <div class="w">
            <div class="date">{{blog.uploadTime | day}}</div>
            <svg class="icon" aria-hidden="true">
              <use use xlink:href="#icon-calender"></use>
            </svg>{{blog.uploadTime|date}}
          </div>
          <svg class="icon" aria-hidden="true">
            <use use xlink:href="#icon-yuedu"></use>
          </svg>{{blog.views}}
          <svg class="icon" aria-hidden="true">
            <use use xlink:href="#icon-dianzan2"></use>
          </svg>{{blog.likes}}
      </div>
      <div class="tags" ref="t">
        <div class="tag" v-for="(t,i) in blog.tags" :key="i">{{t}}</div>
      </div>
      <div class="desc">{{blog.preview}}</div>
    </div>
    <mavon-editor ref="md" :ishljs='false' codeStyle='atom-one-dark' v-model="value" :subfield='false' :editable='false' :toolbarsFlag='false'  defaultOpen="preview" :navigation='true'/>
  </div>
</template>

<script>
// import Blog from 'blogs/blog.md'
import {mapGetters} from 'vuex'
import {getBlogFile, changeLikes} from 'api/blog'
import hljs from 'highlight.js'
export default {
  name: 'mdView',
  components: {},
  props: {},
  data () {
    return {
      value: '',
      blog: {},
      blogId: null,
      like: false
    }
  },
  watch: {
    blogs (newVal) {
      newVal.forEach((item) => {
        if (item.blogId === this.blogId) this.blog = item
      })
    }
  },
  filters: {
    date: (val) => {
      return val ? val.split(' ')[0] : 0
    },
    day: (val) => {
      return val ? val.split(' ')[0].split('-')[2] : 0
    }
  },
  computed: {
    ...mapGetters([
      'blogs'
    ])
  },
  methods: {
    tolike () {
      this.like = true
      this.blog.likes += 1
      changeLikes(this.blog.blogId, true)
    },
    tounlike () {
      this.like = false
      this.blog.likes -= 1
      changeLikes(this.blog.blogId, false)
    },
    back () {
      this.$router.go(-1)
    }
  },
  created () {
    this.blogId = this.$route.params.blogid
    getBlogFile(this.blogId).then((res) => {
      if (res.data === '') {
        alert('博客不存在')
        this.$router.go(-1)
      } else {
        this.value = res.data
      }
    }).then(() => {
      setTimeout(() => {
        let blocks = this.$el.querySelectorAll('pre code')
        blocks.forEach((block) => hljs.highlightBlock(block))
      }, 500)
      setTimeout(() => {
        let eleSelect = document.getElementById('selRate')
        let video = document.getElementById('video')
        if (eleSelect && video) {
          eleSelect.addEventListener('change', function () {
            video.playbackRate = this.value
          })
        }
      }, 2000)
    })
  },
  mounted () {
    this.blogs.forEach((item) => {
      if (item.blogId === this.blogId) this.blog = item
    })
  }
}
</script>

<style  lang="scss" scope>
.md-view{
  width: 100%;
  position: relative;
  background-color: var(--color);
  overflow: hidden;
  border-radius: 8px;
  .title {
    overflow: hidden;
    text-align: center;
    padding: 15px;
    padding-bottom: 15px;
    .text {
      font-size: 2.8rem;
      font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Helvetica,Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol";
      font-weight: 600;
    }
    &:hover > .back {
    opacity: 1;
    animation: rotate 2s linear infinite;
  }
  .date {
    position: absolute;
    width: 21px;
    left: 1px;
    top: 5px;
    font-size: 16px;
  }
  .svgs {
    display: flex;
    justify-content: center;
    font-size: 20px;
    margin-top: 12px;
    .w {
      position: relative;
      svg {
        margin-left: 0;
        margin-right: 8px;
        @media screen and (max-width:1200px){
          margin-right: 0;
        }
      }
    }
    svg {
      margin-left: 40px;
      margin-right: 5px;
      @media screen and (max-width:1200px){
        margin-left: 10px;
        margin-right: 0;
      }
      font-size: 25px;
    }
  }
  .like {
    position: absolute;
    right: 10px;
    top: 10px;
    transform-origin: bottom;
    &:hover {
      animation: shake 0.3s linear 2;
    }
    svg {
      color: transparent;
      font-size: 40px;
    }
  }
  .tags {
    margin-top: 5px;
    margin-bottom: 5px;
  }
  .tag {
    font-size: 18px;
    line-height: 25px;
    background-color: rgba(31, 151, 221, 0.315);
    display: inline-block;
    margin-left: 10px;
    margin-right: 10px;
    border-radius: 5px;
    padding: 1px 8px;
    border: 1px solid rgba(134, 219, 131, 0.7);
    opacity: 0.8;
    &:hover {
      opacity: 1;
      background-color: rgba(22, 155, 231, 0.6);
    }
  }
  .desc {
    font-size: 20px;
    margin-top: 10px;
    margin-left: 40px;
    margin-right: 40px;
    @media screen and (max-width:1200px){
      margin: 0;
    }
    border-radius: 8px;
    text-indent: 40px;
    padding: 10px;
    text-align: left;
    box-shadow: 0 1px 10px 3px rgba(0,0,0,0.1);
  }
  .back {
    position: absolute;
    left: 6px;
    top: 6px;
    opacity: 0;
    transition: all 0.5s ease;
    svg {
      font-size: 56px;
    }
    }
  }
}
@keyframes shake {
  0% {
    transform: rotate(0);
  }
  25% {
    transform: rotate(15deg);
  }
  50% {
    transform: rotate(0deg);
  }
  75% {
    transform: rotate(-15deg);
  }
  100% {
    transform: rotate(0deg);
  }
}
@keyframes rotate {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
.markdown-body .highlight pre,
.markdown-body pre {
  background-color: rgba(0, 0, 0, calc(var(--opacity)*2)) !important;
  color: #fff;
}
.v-note-wrapper, .v-note-op, .v-show-content{
  background-color: var(--color) !important;
}
.v-note-navigation-wrapper {
  border-radius: 8px;
  border: 1px solid black !important;
  right: 12px !important;
  bottom: auto !important;
  height: auto !important;
  width: 225px !important;
  opacity: 0;
  transition: all 0.5s;
  img {
    display: none !important;
  }
  &:hover {
    opacity: var(--opacity);
  }
}
.v-show-content {
  height: 1200px !important;
}
.v-note-wrapper .v-note-panel .v-note-show .v-show-content.scroll-style-border-radius::-webkit-scrollbar, .v-note-wrapper .v-note-panel .v-note-show .v-show-content-html.scroll-style-border-radius::-webkit-scrollbar {
  width: 12px !important;
  background-color: var(--color) !important;
}
.markdown-body .highlight pre, .markdown-body pre {
  padding: 12px !important;
}
.markdown-body blockquote, .markdown-body dl, .markdown-body ol, .markdown-body p, .markdown-body pre, .markdown-body table, .markdown-body ul {
  margin-top: 5px !important;
  margin-bottom: 5px !important;
}
.markdown-body {
  font-size: 20px !important;
}

.markdown-body img {
    margin: 0 auto;
    display: block;
}
</style>
