<template>
  <div class="right-content">
    <board :title='"公告"' :content='user.id === "0" ? user.notice : this.xss(user.notice)'></board>
    <board :title='"标签"'><tags></tags></board>
    <board :title='"博客日历"'><calendar></calendar></board>
  </div>
</template>

<script>
import Tags from 'base/tags/tags'
import Board from 'base/board/board'
import Calendar from 'base/calendar/calendar'
import {mapGetters} from 'vuex'
import xss from 'xss'
export default {
  components: {
    Tags,
    Board,
    Calendar
  },
  props: {},
  data () {
    return {
      xss: xss,
      tip: `有什么问题和Bug欢迎Github留言。<br>&emsp;&emsp;留言板和评论功能日后完善。<br>&emsp;&emsp;第一次登录图片是一样的(图片接口问题)，刷新一下就好了~`
    }
  },
  watch: {},
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  methods: {},
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.right-content{
  margin: calc(var(--margin) * 1px);
  width: 300px;
  @media screen and (max-width:1200px){
    display: none;
  }
}
</style>
