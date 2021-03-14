<template>
  <div class="bottom">
    本站已上线: {{time}}
    <div><a href="http://beian.miit.gov.cn/">浙ICP备2020030909号</a></div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data () {
    return {
      time: 0,
      timer: null
    }
  },
  watch: {},
  computed: {},
  methods: {
    currentTime () {
      this.timer = setInterval(this.getDuration, 500)
    },
    getDuration () {
      let myTime = new Date((new Date().getTime() - 1598363764630))
      let days = myTime / 1000 / 60 / 60 / 24
      let daysRound = Math.floor(days)
      let hours = myTime / 1000 / 60 / 60 - (24 * daysRound)
      let hoursRound = Math.floor(hours)
      let minutes = myTime / 1000 / 60 - (24 * 60 * daysRound) - (60 * hoursRound)
      let minutesRound = Math.floor(minutes)
      let seconds = Math.floor(myTime / 1000 - (24 * 60 * 60 * daysRound) - (60 * 60 * hoursRound) - (60 * minutesRound))
      this.time = daysRound + '天' + hoursRound + '时' + minutesRound + '分' + seconds + '秒'
    }
  },
  created () {
    this.currentTime()
  },
  mounted () {},
  beforeDestroy () {
    if (this.timer) {
      clearInterval(this.timer)
    }
  }
}
</script>
<style  lang="scss" scoped>
.bottom{
  position: absolute;
  bottom: 0;
  right: 5.5%;
  color: #fff;
  text-align: right;
  a {
    color: #fff;
    text-decoration: none;
  }
}
</style>
