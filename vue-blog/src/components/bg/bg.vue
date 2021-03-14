<template>
  <div class="bg" :style="{backgroundPositionY: (1 - rate) * 100 + '%',backgroundImage: url? `url(${url})`: ``}"></div>
</template>

<script>
export default {
  components: {},
  props: {
    rate: {
      type: Number
    },
    url: {
      type: String
    }
  },
  data () {
    return {}
  },
  watch: {},
  computed: {},
  methods: {},
  created () {},
  mounted () {}
}
</script>

<style>
.bg{
  position: fixed;
  top: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  background-color: black;
  background-size: cover;
  /* background-image: url('../../assets/rainy2.jpg'); */
  background-position: center;
  background-position-y: 100%;
}
</style>
