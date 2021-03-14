<template>
  <div class="board">
    <div class="title">
      {{title}}
    </div>
    <div class="line"></div>
    <slot></slot>
    <p v-html="content"></p>
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    content: {
      type: String
    },
    title: {
      type: String
    }
  },
  data () {
    return {}
  },
  watch: {},
  computed: {},
  methods: {}
}
</script>
<style  lang="scss" scoped>
.board{
  position: relative;
  // height: 420px;
  height: auto;
  background-color: var(--color);
  margin-bottom: 20px;
  border-radius: 9px;
  display: flex;
  flex-direction: column;
  user-select: none;
  align-items: center;
  width: 100%;
  .title {
    letter-spacing: 1px;
    color: #000;
    padding-top: 4px;
    font-weight: 700;
    font-size: 24px;
    margin-bottom: 4px;
    height: 32px;
  }
  .line {
    width: 92%;
    height: 2px;
    background-color: #000;
    margin: 0 auto 0px auto;
  }
  p {
    user-select: text;
    width: calc(100% - 20px);
    display: block;
    letter-spacing:1px;
    text-indent: 42px;
    margin: 8px;
    margin-top: 6px;
    line-height: 30px;
    word-wrap: break-word;
    // word-break: break-all;
    font-size: 21px;
  }
}
</style>
