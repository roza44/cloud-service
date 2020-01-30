Vue.component("disc_search", {

    data: function() {
        return {
            name : "",
            cpuLow : null,
            cpuHigh : null,
            type : ""
        };
    },

    template : `
    <div>
        <h2 class="text-muted">Pretraga diskova</h2>
        <form style="padding-top:20px;padding-left:5px;padding-right:5px">
            <div class="form-group" style="width:96%">
                <label for="imeSearch">Ime</label>
                <input type="text" class="form-control" id="imeSearch" v-model="name">
            </div>
            <div class="form-group" style="display:flex">
                <div style="width:48%">
                    <label for="lowLimitC">CPU min</label>
                    <input id="lowLimitC" type="number" class="form-control" v-model="cpuLow">
                </div>
                <div style="width:48%">
                    <label for="highLimitC">CPU max</label>
                    <input id="highLimitC" type="number" class="form-control" v-model="cpuHigh">
                </div>
            </div>
            <div class="form-group" style="width:96%">
                <label for="typeSearch">Tip</label>
                <input type="text" class="form-control" id="typeSearch" v-model="type">
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
            this.$emit("search", this.formToJson());
        },

        reset : function() {

        },

        formToJson : function() {
            return {"name" : this.name, "cpuLow" : this.cpuLow, "cpuHigh": this.cpuHigh, "type" : this.type};
        }
    }

});