<template>
    <div>
        <div ref="wrapper"></div>
        <template v-if="nodes.length && dataTree">
            <template v-for="node in nodes">
                <node-wrapper :key="node.__data__.data.id" :n="node" :y="-15" :x="-10">
                    <tree-node :n="node" :folds="folds" />
                </node-wrapper>
            </template>
        </template>
        <control-panel @onFold="onFold" />
    </div>
</template>
<script>
import * as d3 from "d3";
import NodeWrapper from "./NodeWrapper";
import TreeNode from "./TreeNode";
import ControlPanel from "./ControlPanel";
export default {
    components: { NodeWrapper, TreeNode, ControlPanel },
    props: {
        dataTree: {
            type: Array,
            required: true
        },
        label: {
            type: Function,
            required: true
        }
    },
    data() {
        return {
            folds: {},
            nodes: [],
            treeType: "seq-errors",
            currentDepth: 10
        };
    },
    mounted () {
        this.initData()
        this.reset()
    },
    methods: {
        initData() {
            for (const node of this.dataTree) {
                this.folds[node.task.id] = node.isFolded;
            }
        },
        onFold(nodeId) {
            this.folds[nodeId] = !this.folds[nodeId];
            this.reset();
        },
        setTreeType(type) {
            this.treeType = type;
            this.initData();
            setTimeout(() => {
                this.reset();
            }, 300);
        },
        reset() {
            d3.select("#topology").remove();
            this.$refs.wrapper.innerHTML = '<div id="topology"></div>';
            this.buildTree();
        },
        onNodeClick(node) {
            this.$emit("onNodeClick", node.data);
        },
        buildTree() {
            const data = this.virtualTree;
            this.nodes = [];
            // set the dimensions and margins of the graph
            var width = 460;
            var height = 460;

            // append the svg object to the body of the page
            const zoom = d3.zoom().on("zoom", () => {
                const t = d3.event.transform;
                svg.attr("transform", `translate(${t.x},${t.y}) scale(1)`);
                this.$store.dispatch("graph/updateConfigurationPanelPosition");
            });

            var svg = d3
                .select("#topology")
                .append("svg")
                .attr("width", "100%")
                .attr("height", "60vh")
                .call(zoom)
                .append("g")
                .attr("transform", "translate(25,0)"); // bit of margin on the left = 40

            // read json data
            // Create the cluster layout:
            var cluster = d3.cluster().size([height, width - 100]); // 100 is the margin I will have on the right side
            // Give the data to this cluster layout:
            let count = 0;
            var root = d3.hierarchy(data, d => {
                count++;
                return this.folds[d.task.id] ? undefined : d.children;
            });
            cluster(root);

            const yFactor = parseInt(count / 2) * 0.9;
            // Add the links between nodes:
            const existingNode = {};
            const existingPath = {};
            svg.selectAll("path")
                .data(root.descendants().slice(1))
                .enter()
                .append("path")
                .attr("d", d => {
                    if (existingPath[d.data.task.id]) {
                        const p = existingPath[d.data.task.id];
                        return `M${p.y},${p.x}C${p.y},${p.x} ${d.parent.y *
                            yFactor},${d.parent.x} ${d.parent.y * yFactor +
                            100},${d.parent.x}`;
                    } else {
                        existingPath[d.data.task.id] = {
                            y: d.y * yFactor,
                            x: d.x
                        };
                        return `M${d.y * yFactor},${d.x}C${d.parent.y + 150},${
                            d.x
                        } ${d.parent.y * yFactor},${d.parent.x} ${d.parent.y *
                            yFactor},${d.parent.x}`;
                    }
                })
                .style("fill", "none")
                .attr("stroke", "#ccc")
                .attr("stroke-width", "5");
            svg.selectAll("g")
                .data(root.descendants())
                .enter()
                .append("g")
                .attr("transform", d => {
                    if (existingNode[d.data.task.id]) {
                        return existingNode[d.data.task.id];
                    } else {
                        const operation = `translate(${d.y * yFactor},${d.x})`;
                        existingNode[d.data.task.id] = operation;
                        return operation;
                    }
                });
            svg.selectAll("g")
                .nodes()
                .forEach(n => this.nodes.push(n));
            window.$nb = this.nodes;
        }
    },
    computed: {
        virtualTree() {
            const parentTree = {};
            let root = undefined;
            for (const node of this.dataTree) {
                for (const parent of node.parent || []) {
                    if (!parentTree[parent.id]) {
                        parentTree[parent.id] = [];
                    }
                    parentTree[parent.id].push(
                        JSON.parse(JSON.stringify(node))
                    );
                }
                if (!node.parent) {
                    root = node;
                }
            }
            const growTree = (parent, depth) => {
                if (depth == this.currentDepth) {
                    return;
                }
                for (const child of parentTree[parent.task.id] || []) {
                    if (!parent.children) {
                        parent.children = [];
                    }
                    parent.children.push(child);
                    growTree(child, depth + 1);
                }
            };
            growTree(root, 0);
            return root;
        }
    }
};
</script>
<style scoped>
text {
    font-family: monospace !important;
}
</style>
