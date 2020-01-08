<template>
    <svg v-if="n" :x="x" :y="y">
        <foreignObject :height="h" :width="w">
            <slot />
        </foreignObject>
    </svg>
</template>
<script>
export default {
    props: {
        n: {
            type: SVGElement,
            default: undefined
        },
        x: {
            type: Number,
            default: 0
        },
        y: {
            type: Number,
            default: 0
        },
        w: {
            type: Number,
            default: 250
        },
        h: {
            type: Number,
            default: 350
        }
    },
    mounted() {
        this.append();
    },
    watch: {
        n() {
            this.append();
        }
    },
    methods: {
        append() {
            if (this.n && !this.n.contains(this.$el)) {
                setTimeout(() => {
                    this.n.appendChild(this.$el);
                });
            }
        }
    }
};
</script>