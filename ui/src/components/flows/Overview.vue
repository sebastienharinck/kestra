<template>
    <div>
        <b-row class="topology-wrapper">
            <b-col>
                <topology-tree :isFlow="true" v-if="flow && dataTree" :dataTree="dataTree" :label="getLabel"/>
            </b-col>
        </b-row>
        <b-row>
            <b-col>
                <task-details/>
            </b-col>
        </b-row>
    </div>
</template>
<script>
import { mapState } from "vuex";
import TopologyTree from "../graph/TopologyTree";
import TaskDetails from "./TaskDetails";

export default {
    components: {
        TopologyTree,
        TaskDetails
    },
    computed: {
        ...mapState("flow", ["flow", "dataTree"]),
    },
    methods: {
        getLabel (node) {
            const id = node.data.id;
            return `${id.substr(0, 25)}${id.length > 25 ? "..." : ""}`;
        }
    }
};
</script>
<style lang="scss" scoped>
.topology-wrapper {
    border: 1px solid #bbb;
    padding: 0;
    margin: 0;
    .col {
        padding: 0;
        margin: 0;
    }
}
</style>