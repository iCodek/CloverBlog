<template>
  <div class="tips" :style="{top:y+'px',left:x+'px'}" @click="tip">
    <transition name="fade-out">
      <div class="temp1" :style="{backgroundImage : 'url(https://api.66mz8.com/api/rand.portrait.php?type=女&t='+t1+')'}" v-show="s1">
      </div>
    </transition>
    <transition name="fade-out">
      <div class="temp2" :style="{backgroundImage : 'url(https://api.66mz8.com/api/rand.portrait.php?type=女&t='+t2+')'}" v-show="s2">
      </div>
    </transition>
  </div>
</template>

<script>
// http://api.btstu.cn/sjtx/api.php
// https://www.icnal.com/sjtx/api.php
// https://api.66mz8.com/api/rand.portrait.php?type=女
export default {
  components: {},
  props: {
    time: {
      type: Number,
      default () {
        return 0
      }
    },
    x: {
      type: Number,
      default () {
        return 0
      }
    },
    y: {
      type: Number,
      default () {
        return 0
      }
    }
  },
  data () {
    return {
      s1: false,
      s2: true,
      t1: 0,
      t2: 0
    }
  },
  watch: {
    time () {
      this.refresh()
    }
  },
  computed: {},
  methods: {
    refresh () {
      this.s1 = !this.s1
      this.s2 = !this.s2
      if (this.s1) this.t2 += 1
      else this.t1 -= 1
    },
    tip () {
      this.refresh()
    }
  },
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.tips{
  user-select: none;
  position: fixed;
  z-index: 2000;
  transform: translate(-50%, -50%);
  .temp1 {
    opacity: 0;
    background-size: cover;
    border-radius: 50%;
  }
  .temp2 {
    opacity: 0;
    background-size: cover;
    border-radius: 50%;
  }
  .fade-out-enter-active {
    transition: all 1s cubic-bezier(.25,.72,.36,1);
    height: 120px;
    width: 120px;
  }
  .fade-out-enter {
    transform: translateY(20px);
    opacity: 0;
  }
  .fade-out-enter-to {
    transform: translateY(-80px);
    opacity: 1;
  }
}
</style>
