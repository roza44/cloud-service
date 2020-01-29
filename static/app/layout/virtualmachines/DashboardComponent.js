Vue.component("dashboard", {

    data: function() {
        return {
            role : ""
        };
    },

    template: `
        <default_layout>
            <vm_list @changeVM="showChange($event)" ref="list"></vm_list>
            <div class="container-fluid">
                <button v-if="role==='admin' || role==='superadmin'" v-on:click="showAdd" type="button" class="btn btn-outline-primary btn-lg btn-block">
                Dodaj virualnu masinu
                </button>
            </div>
            <vm_modal @deleteList="deleteList($event)" @changeList="changeList($event)" @addVM="addVM($event)" ref="vmModal"></vm_modal>
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
        }
        
    },

    mounted() {
        this.role = localStorage.getItem('role');
    }
    
});