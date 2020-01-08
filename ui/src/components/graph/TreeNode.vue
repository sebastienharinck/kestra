<template>
    <div class="wrapper" :class="wrapperCls">
        <div class="status-color" :class="contentCls"></div>
        <div class="task-content">
            <div class="card-header">
                <div class="icon-wrapper">
                    <img
                        src="https://cloud-cdn.safe.com/fmehub/fmepackageversion/safe/google-bigquery/item-logo/1564603216.png"
                    />
                </div>
                <b-tooltip
                    placement="bottom"
                    :target="`node-${task.id}`"
                    triggers="hover"
                >{{task.id}}</b-tooltip>
                <span
                    :id="`node-${task.id}`"
                    class="text-monospace task-title"
                >{{task.id | ellipsis(18)}}</span>
                <menu-open class="node-action" @click="onSettings" />
            </div>
            <div v-if="task.state" class="status-wrapper">
                <status :status="state"/>
            </div>
        </div>
    </div>
</template>
<script>
import MenuOpen from "vue-material-design-icons/MenuOpen";
import { mapState } from "vuex";
import Status from "../Status";

export default {
    components: { Status, MenuOpen },
    props: {
        n: {
            type: SVGElement,
            default: undefined
        },
        folds: {
            type: Object,
            required: true
        }
    },
    methods: {
        onSettings() {
            if (this.node) {
                this.$store.dispatch("graph/setNode", undefined);
            } else {
                this.$store.dispatch("graph/setNode", this.n);
                this.$emit("onSettings");
            }
        }
    },
    computed: {
        ...mapState("graph", ["node"]),
        state () {
            return this.task.state ? this.task.state.current : "SUCCESS";
        },
        contentCls() {

            return {
                "is-success": !["RUNNING", "FAILED"].includes(this.state),
                "is-running": this.state === "RUNNING",
                "is-failed": this.state === "FAILED"
            };
        },
        wrapperCls() {
            return {
                "is-container": this.folds[this.task.id]
            };
        },
        task() {
            return this.n.__data__.data.task;
        }
    }
};
</script>
<style scoped lang="scss">
@import "../../styles/_variable.scss";
.wrapper.is-container {
    border: 2px dashed $purple;
    border-radius: 2px;
}
.wrapper {
    padding: 5px;
    cursor: pointer;
    .status-color {
        width: 20px;
        height: 75px;
        float: left;
        border: 1px solid $dark;
        border-right: none;
    }

    .is-success {
        background-color: $green;
    }
    .is-running {
        background-color: $blue;
    }
    .is-failed {
        background-color: $red;
    }

    .task-content {
        background-color: $white;
        border: 1px solid $dark;
        margin-left: 20px;
        height: 75px;
        .card-header {
            width: 100%;
            height: 30px;
            padding: 2px;
            margin: 0px;
            border-bottom: 1px solid $dark;

            .icon-wrapper img {
                width: 25px;
                height: 25px;
                float: left;
            }
            .task-title {
                margin-left: 5px;
            }
            .node-action {
                float: right;
                padding-top: 18px;
                padding-right: 18px;
            }
        }
        .status-wrapper {
            margin: 10px;
        }
    }

    .card-wrapper {
        top: 50px;
        position: absolute;
    }
}
</style>