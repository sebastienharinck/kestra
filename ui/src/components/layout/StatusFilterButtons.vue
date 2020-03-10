<template>
    <b-dropdown id="dropdown-1" :text="$t('status')" class="m-md-2" variant="light">
        <b-dropdown-item href="#">
            <b-button class="rounded-lg" variant="primary">
                <record-circle-outline />
                {{$t('all') | cap}}
            </b-button>
        </b-dropdown-item>
        <b-dropdown-item href="#" v-for="status in statuses" :key="status">
            <status @click.native="searchStatus(status)" class="status sm" :status="status" />
        </b-dropdown-item>
    </b-dropdown>
</template>
<script>
import Status from "../Status";
import RecordCircleOutline from "vue-material-design-icons/RecordCircleOutline";

export default {
    components: { Status, RecordCircleOutline },
    data() {
        return {
            statuses: ["RUNNING", "SUCCESS", "FAILED"]
        };
    },
    methods: {
        searchStatus(status) {
            const token = status.toUpperCase();
            if (this.$route.query.q !== token) {
                this.$router.push({
                    query: { ...this.$route.query, q: token }
                });
                this.$emit("onRefresh", token);
            }
        }
    }
};
</script>
