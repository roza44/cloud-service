Vue.component("vm_search", {

    data : function() {
        return {
            name : "",
            cat : "",
            cpuLow : null,
            cpuHigh : null,
            ramLow : null,
            ramHigh : null,
            gpuLow : null,
            gpuHigh : null
        };
    },

    template : `
    <div>
        <h2 class="text-muted">Pretraga virtualnih masina</h2>
        <form style="padding-top:20px;padding-left:5px;padding-right:5px">
            <div class="form-group" style="width:96%">
                <label for="imeVmSearch">Ime</label>
                <input type="text" class="form-control" id="imeVmSearch" v-model="name">
            </div>
            <div class="form-group" style="width:96%">
                <label for="katVmSearch">Kategorija</label>
                <input type="text" class="form-control" id="katVmSearch" v-model="cat">
            </div>
            <div class="form-group" style="display:flex">
                <div style="width:48%">
                    <label for="lowLimitCPU">CPU min</label>
                    <input id="lowLimitCPU" type="number" class="form-control" v-model="cpuLow">
                </div>
                <div style="width:48%">
                    <label for="highLimitCPU">CPU max</label>
                    <input id="highLimitCPU" type="number" class="form-control" v-model="cpuHigh">
                </div>
            </div>
            <div class="form-group" style="display:flex">
                <div style="width:48%">
                    <label for="lowRAM">RAM min</label>
                    <input id="lowRAM" type="number" class="form-control" v-model="ramLow">
                </div>
                <div style="width:48%">
                    <label for="highRAM">RAM max</label>
                    <input id="highRAM" type="number" class="form-control" v-model="ramHigh">
                </div>
            </div>
            <div class="form-group" style="display:flex">
                <div style="width:48%">
                    <label for="lowLimitGPU">GPU min</label>
                    <input id="lowLimitGPU" type="number" class="form-control" v-model="gpuLow">
                </div>
                <div style="width:48%">
                    <label for="highLimitGPU">GPU max</label>
                    <input id="highLimitGPU" type="number" class="form-control" v-model="gpuHigh">
                </div>
            </div>
           
            <div class="form-group" style="display:flex">
                <div style="width:48%">
                    <button v-on:click="search" type="button" class="btn btn-outline-primary btn-lg btn-block">
                        Pretrazi
                    </button>
                </div>
                <div style="width:48%">
                    <button v-on:click="reset" type="button" class="btn btn-outline-primary btn-lg btn-block">
                        Osnovni prikaz
                    </button>
                </div>
            </div>
        </form>
    </div>
    `,

    methods : {
        search : function() {
            this.$emit("searchVMs", this.formToJson());
        },

        reset : function() {
            this.$emit("resetVMs");
        },

        formToJson : function() {
            return {"name" : this.name, "cat" : this.cat, "cpuLow" : this.cpuLow, "cpuHigh" : this.cpuHigh,
                    "ramLow" : this.ramLow, "ramHigh" : this.ramHigh, "gpuLow" : this.gpuLow, "gpuHigh" : this.gpuHigh};
        }
    }

});