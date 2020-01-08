<template>
    <b-card v-if="nodeData" :title="`${this.$t('task').capitalize()} → ${task.id}`">
        <b-card-text>
            <b-table class="table-sm" striped hover :items="items">
                <template v-slot:cell(value)="row">
                    <div v-html="row.item.value" />
                </template>
            </b-table>
        </b-card-text>
    </b-card>
</template>
<script>
import Yaml from "yaml";
import { mapGetters } from "vuex";
export default {
    computed: {
        ...mapGetters("graph", ["nodeData"]),
        task() {
            return this.nodeData.task;
        },
        items() {
            const items = [];
            for (const property in this.task) {
                const v = this.task[property];
                const value =
                    typeof v === "object"
                        ? `<pre>${Yaml.stringify(v)}</pre>`
                        : v;
                items.push({ property, value });
            }
            return items;
        }
    }
};
</script>