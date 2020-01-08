<template>
    <div v-if="node" class="control-wrapper" :style="style">
        <b-card class="mb-2">
            <b-button class="action-button" v-if="nodeData.children" href="#" @click="onFold" variant="primary" size="sm">{{$t('fold') | cap}} / {{$t('unfold')}}</b-button>
        </b-card>
    </div>
</template>
<script>
import { mapState, mapGetters } from "vuex";
export default {
    methods: {
        onFold() {
            this.$emit("onFold", this.nodeData.task.id);
            this.$store.dispatch('graph/setNode', undefined)
        }
    },
    computed: {
        ...mapState("graph", ["configurationPanelPosition", "node"]),
        ...mapGetters("graph", ["nodeData"]),
        style() {
            return { left: this.configurationPanelPosition.right + "px", top: this.configurationPanelPosition.top + "px" };
        },
    }
};
</script>
<style scoped lang="scss">
.control-wrapper {
    position: absolute;
    z-index: 1000;
    width: 140px;

    .card-body {
        padding: 5px;
    }
    .action-button {
        width: 100%;
    }
}
</style>