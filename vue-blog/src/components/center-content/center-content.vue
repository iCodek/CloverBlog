<template>
  <div class="center-content">
    <tab></tab>
    <keep-alive exclude="mdView">
      <router-view></router-view>
    </keep-alive>
  </div>
</template>

<script>
import Tab from 'cpnts/tab/tab'
import MainContent from 'cpnts/main-content/main-content'
export default {
  components: {
    Tab,
    MainContent
  },
  props: {},
  data () {
    return {
    }
  },
  watch: {},
  computed: {},
  methods: {},
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.center-content{
  position: relative;
  margin: calc(var(--margin) * 1px) calc(var(--margin) * 2px);
  flex: 1;
  overflow: hidden;
}
</style>
