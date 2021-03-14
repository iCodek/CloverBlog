<template>
  <div class="center" @click.self="tip">
    <left-content></left-content>
    <center-content></center-content>
    <right-content></right-content>
    <tips :time="at" :x="x" :y="y"></tips>
  </div>
</template>

<script>
import LeftContent from 'cpnts/left-content/left-content'
import CenterContent from 'cpnts/center-content/center-content'
import RightContent from 'cpnts/right-content/right-content'
import tips from 'cpnts/tips/tips'
export default {
  components: {
    LeftContent,
    CenterContent,
    RightContent,
    tips
  },
  props: {},
  data () {
    return {
      at: 0,
      x: 0,
      y: 0
    }
  },
  watch: {},
  computed: {},
  methods: {
    tip (e) {
      this.at = new Date().getTime()
      this.x = e.clientX
      this.y = e.clientY
    }
  },
  created () {},
  mounted () {}
}
</script>
<style lang="scss" scoped>
.center{
  position: relative;
  display: flex;
  align-items:flex-start;
  height: 100%;
  width: 90%;
  margin: calc(var(--margin) * 1px) auto;
  padding-bottom: 160px;
  @media screen and (max-width:1200px){
    width: 96%;
  }
}
</style>
