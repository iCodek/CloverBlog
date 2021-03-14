<template>
  <div :class="cls">
    <div class="close" @click="isShow=!isShow">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-siyecaox"></use>
      </svg>
    </div>
    <div v-show="isShow">
      <user-info></user-info>
      <music-box></music-box>
    </div>
  </div>
</template>

<script>
import UserInfo from 'cpnts/user-info/user-info'
import MusicBox from 'cpnts/music-box/music-box'
export default {
  components: {
    UserInfo,
    MusicBox
  },
  props: {},
  data () {
    return {
      isShow: true
    }
  },
  watch: {},
  computed: {
    cls () {
      return this.isShow ? 'left-content' : 'left-content left-close'
    }
  },
  methods: {},
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.left-content{
  position: relative;
  margin: calc(var(--margin) * 1px);
  width: 360px;
  transition: all 0.5s;
  @media screen and (max-width:1200px){
    display: none;
  }
  &:hover > .close {
    opacity: 1;
    animation: rotate 2s linear infinite;
  }
  .close {
    z-index: 1;
    width: 40px;
    height: 40px;
    position: absolute;
    top: 0;
    opacity: 0;
    transition: all 0.5s ease;
  }
  @keyframes rotate {
    0% {
      transform: rotate(0);
    }
    100% {
      transform: rotate(360deg);
    }
  }
}
.left-close {
  width: 40px;
  height: 80px;
}
</style>
