Vue.component("dashboard", {

    data: function() {
        return {
            role : ""
        };
    },

    template: `
        <default_layout>
            <vm_list ref="list"></vm_list>
            <div class="container-fluid">
                <button v-if="role==='admin' || role==='superadmin'" v-on:click="showAdd" type="button" class="btn btn-outline-primary btn-lg btn-block">
                Dodaj virualnu masinu
                </button>
            </div>
            <vm_modal @addVM="addVM($event)" ref="vmModal"></vm_modal>
        </default_layout>
    `,

    methods : {

        showAdd : function() {
            this.$refs.vmModal.show(null);
        },

        showChange : function(model) {

        },

        addVM : function(vm) {
            this.$refs.list.add(vm);
        }
        
    },

    mounted() {
        this.role = localStorage.getItem('role');
    }
    
});