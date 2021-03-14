<template>
  <div class="m-header">
    <div class="headerbg" :style="{backgroundImage: url?`url(${url})`:``}"></div>
    <div class="container">
        <svg
            width="300"
            height="100"
            class="text">
            <defs>
                <linearGradient id="geekColor" gradientTransform="rotate(90)">
                    <stop offset="0%" stop-color="#91bd3a">
                        <animate
                        attributeName="stop-color"
                        values="#91bd3a;#12cad6;#91bd3a"
                        dur="10s"
                        begin="3s"
                        repeatCount="indefinite"
                        ></animate>
                    </stop>
                    <stop offset="100%" stop-color="#12cad6">
                        <animate
                        attributeName="stop-color"
                        values="#12cad6;#91bd3a;#12cad6"
                        dur="10s"
                        begin="3s"
                        repeatCount="indefinite"
                        ></animate>
                    </stop>

                </linearGradient>
            </defs>
            <text class="logo_text"
                x="0"
                y="2.5rem">
                CloverBlog
            </text>
        </svg>
    </div>
    <div class="loc">
      <progress-bar :min=0 :max=100 v-model = "transparent"></progress-bar>
    </div>
    <search-box></search-box>
    <essearch></essearch>
  </div>
</template>

<script>
import SearchBox from 'cpnts/search-box/search-box'
import ProgressBar from 'base/progress-bar/progress-bar'
import Essearch from '../essearch/essearch.vue'
export default {
  components: {
    SearchBox,
    ProgressBar,
    Essearch
  },
  props: {
    url: {
      type: String
    }
  },
  data () {
    return {
      transparent: 0,
      s: false
    }
  },
  watch: {
    transparent (newVal) {
      if (newVal >= 0 && newVal <= 100) {
        let root = document.querySelector(':root')
        let value = '--color: ' + 'rgba(255, 255, 255, ' + (1 - newVal / 100) + ');'
        let opacity = '--opacity: ' + (1 - newVal / 100)
        root.setAttribute('style', value + opacity)
      }
    }
  },
  computed: {},
  methods: {},
  created () {},
  mounted () {}
}
</script>
<style lang="scss" scoped>
.m-header {
  position: relative;
  .headerbg {
    background-color: #000;
    // background-image: url('../../assets/a.jpg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center bottom;
    width: 100%;
    height: 125px;
    opacity: var(--opacity);
  }
  .loc {
    position: absolute;
    bottom: 100px;
    left: -10px;
  }
  .container {
    @media screen and (max-width:1200px){
      display: none;
    }
    position: absolute;
    top: 0;
    left: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
    .logo_text {
      font-family: "PingFang SC";
      font-size: 2.5rem;
      fill: none;
      stroke: url(#geekColor);
      stroke-width: 1;
      stroke-dasharray: 10 800;
      stroke-dashoffset: 10;
      animation: 12s linear 5s drawing forwards;
    }
  }
  @keyframes drawing {
    to {
        stroke-dasharray: 800;
        stroke-dashoffset: 0;
    }
  }
}
</style>
