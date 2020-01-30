Vue.component("dashboard", {

    data: function() {
        return {
            role : ""
        };
    },

    template: `
        <default_layout>
            <div class="containter-fluid">
                <div class="row">
                    <div class="col-8">
                        <vm_list @changeVM="showChange($event)" ref="list"></vm_list>
                        <button v-if="role==='admin' || role==='superadmin'" v-on:click="showAdd" type="button" class="btn btn-outline-primary btn-lg btn-block">
                        Dodaj virualnu masinu
                        </button>
                        <vm_modal @deleteList="deleteList($event)" @changeList="changeList($event)" @addVM="addVM($event)" ref="vmModal"></vm_modal>
                    </div>
                    <div class="col-4">
                        <vm_search @resetVMs="resetVMs" @searchVMs="searchList($event)"></vm_search>
                    </div>
                </div>
            </div>
        </default_layout>
    `,

    methods : {

        showAdd : function() {
            this.$refs.vmModal.show(null);
        },

        showChange : function(model) {
            this.$refs.vmModal.show(model);
        },

        addVM : function(vm) {
            this.$refs.list.add(vm);
        },

        deleteList : function(vm) {
            this.$refs.list.delete(vm);
        },

        changeList : function(vm) {
            this.$refs.list.change(vm);
        },

        searchList : function(filter) {
            this.$refs.list.searchList(filter);
        },
        
        resetVMs : function() {
            this.$refs.list.resetList();
        }
        
    },

    mounted() {
        this.role = localStorage.getItem('role');
    }
    
});