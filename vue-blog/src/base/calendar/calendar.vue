<template>
  <div class="calendar">
    <ul v-for="(monthBlog,month,index) in sortBlog" :key="month" class="ul ulhide" :style="{height : 32 * monthBlog.length + 36 + 'px'}">
      <div class="month" @click="toggle($event)" :style="{backgroundColor : color[index]}">
        {{month}}
        <span class="count">{{monthBlog.length}}</span>
      </div>
      <li v-for="blog in monthBlog" :key="blog.blogId">
        <!-- <router-link tag="div" class="line" :to="{name: 'blog', params: {id: blog.userId, blogid: blog.blogId}}"> -->
        <div class="line" @click="jump(blog.userId,blog.blogId)">
          <div class="day">{{blog.uploadTime.split(' ')[0].slice(8, 10)}}</div>
          <div class="title">{{blog.title}}</div>
        <!-- </router-link> -->
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {rainbowColor} from 'common/js/util'
export default {
  components: {},
  props: {},
  data () {
    return {
      sortBlog: {},
      count: 0,
      color: []
    }
  },
  watch: {
    blogs (newVal) {
      this.count = 0
      let objs = newVal.reduce((obj, item) => {
        let time = item.uploadTime.split(' ')[0].slice(0, 7)
        if (time in obj) obj[time].push(item)
        else {
          obj[time] = [item]
          this.count++
        }
        return obj
      }, {})
      this.sortBlog = objs
      this.rainBow()
    }
  },
  computed: {
    ...mapGetters([
      'blogs'
    ])
  },
  methods: {
    toggle (e) {
      let dom = e.currentTarget.parentNode
      if (dom.getAttribute('class') === 'ul') dom.setAttribute('class', 'ul ulhide')
      else dom.setAttribute('class', 'ul')
    },
    rainBow () {
      this.color = rainbowColor(this.count, 0, 200, 0.8)
    },
    jump (id, blogId) {
      if (this.$route.name === 'index') this.$router.push({name: 'blog', params: {id: id, blogid: blogId}})
      else this.$router.push({path: '/:id'}).then(() => this.$router.push({name: 'blog', params: {id: id, blogid: blogId}}))
    }
  },
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.calendar {
  width: calc(100% - 16px);
  padding-left: 8px;
  padding-right: 8px;
  .ul {
    margin-top: 2px;
    border: 1px solid rgba(0, 0, 0, 0.5);
    box-sizing: border-box;
    border-radius: 5px;
    font-size: 25px;
    background-color: var(--color);
    overflow: hidden;
    height: auto;
    transition: all 0.4s ease;
    .month {
      opacity: 0.64;
      border-radius: 5px;
      height: 36px;
      padding-left: 4px;
      position: relative;
      color: #fff;
      &:hover {
        opacity: 1;
      }
      .count {
        padding-left: 8px;
        padding-right:8px;
        font-size: 22px;
        text-align: center;
        position: absolute;
        right: 10px;
        top: 3px;
        line-height: 28px;
        color: rgb(0, 0, 0);
        height: 28px;
        border-radius: 50%;
        background-color: rgb(255, 255, 255);
      }
    }
    li {
      height: 30px;
      font-size: 20px;
      line-height: 28px;
      margin-top: 1px;
      margin-bottom: 2px;
      list-style: none;
      padding-left: 4px;
      overflow: hidden;
      .line {
        display: flex;
        .day {
          width: 30px;
        }
        .title {
          padding-left: 5px;
          flex: 1;
        }
      }
      &:hover {
        background-color: rgb(231, 231, 231);
      }
    }
  }
  .ulhide {
    height: 36px !important;
  }
}
</style>
