<template>
  <div class="main-content">
    <blog-box v-for="(blog, index) in blogs" :key="blog.blogId" :blog=blog :index=index></blog-box>
  </div>
</template>

<script>
import BlogBox from 'base/blog-box/blog-box'
import {mapGetters} from 'vuex'
export default {
  components: {
    BlogBox
  },
  props: {},
  data () {
    return {}
  },
  watch: {},
  computed: {
    ...mapGetters([
      'blogs'
    ])
  },
  methods: {},
  created () {},
  mounted () {}
}
</script>
<style lang="scss" scoped>
.main-content{
  text-align: center;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
  align-items: center;
}
</style>
