Vue.component("disk_modal", {
    data : function() {

        return {
            disk : {
                ime : "",
                tip : "",
                kapacitet : "",
                vm : {
                    ime: ""
                },
                organizacija: {
                    ime: ""
                }
            },

            type : null,
            organizations : null,
            virtualMachines : null,
            role : null
        }

    },

    template : `
    <div class="modal fade" id="diskModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 v-if="type==='add'" class="modal-title" id="exampleModalLabel">Novi disk</h5>
                <h5 v-if="type==='change'" class="modal-title" id="exampleModalLabel">{{disk.ime}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div v-if="type==='add'" class="form-group">
                        <label for="formGroupExampleInput1">Ime</label>

                        <input type="text" class="form-control" id="formGroupExampleInput1" v-model="disk.ime" :readonly="role === 'user'">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput2">Tip</label>

                        <input type="text" class="form-control" id="formGroupExampleInput2" v-model="disk.tip" :readonly="role === 'user'">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput3">Kapacitet</label>

                        <input type="text" class="form-control" id="formGroupExampleInput3" v-model="disk.kapacitet" :readonly="role === 'user'">
                    </div>

                    <div v-if="type === 'add'">
                        <label for="orgSelectField">Organizacija</label>
                        <select class="form-control" id="orgSelectField" @change="populateVMs(null)" v-model="disk.organizacija">
                            <option v-for="org in organizations" :value="org">{{org.ime}}</option>
                        </select>
                    </div>

                    <div v-else>
                        <label for="orgViewField">Organizacija</label>
                            
                        <input type="text" class="form-control" id="orgViewField" v-model="disk.organizacija.ime" readonly>
                    </div>

                    <div v-if="role === 'user'">
                        <label for="vmViewField">Virtualna mašina</label>
                        
                        <input v-if="disk.vm == null" type="text" class="form-control" id="vmViewField" readonly>
                        <input v-else type="text" class="form-control" id="vmViewField" v-model="disk.vm.ime" readonly>
                    </div>

                    <div v-else>
                        <label for="vmSelectField">Virtualna mašina</label>
                        <select class="form-control" id="vmSelectField" v-model="disk.vm">
                            <option :value="null">/</option>
                            <option v-for="vm in virtualMachines" :value="vm">{{vm.ime}}</option>
                        </select>
                    </div>
                </form>
            </div>
            <div v-if="role !== 'user' "class="modal-footer">
                <button v-if="type==='add'" v-on:click="add" type="button" class="btn btn-primary">Dodaj</button>
                <button v-if="type==='change'" v-on:click="change" type="button" class="btn btn-primary">Izmeni</button>
                <button v-if="type==='change' && role === 'superadmin'" v-on:click="deleteDisk" type="button" class="btn btn-primary" style="background-color:coral">
                    Izbrisi disk
                </button>
            </div>
            </div>
        </div>
    </div>
    
    `,

    mounted() {
        this.role = localStorage.getItem('role');
    },

    methods: {
        show : function(model, organizations) {
            this.fillContent(model);
            this.organizations = organizations;
            this.populateVMs(function() {
                // then...
                $("#diskModal").modal('show');
            });
        },
        
        populateVMs : function(then) {
            if (!this.disk.organizacija) {
                // If there's no org (adding a new disk)
                this.disk.organizacija = this.organizations[0];
            }
            axios
            .get("rest/Organizations/vms/" + this.disk.organizacija.ime)
            .then(response => {
                this.virtualMachines = response.data;
                if (then) then();
            })
            .catch(function(error) { alert(error); });
        },

        fillContent : function(model) {
            if(model == null) {
                this.type = "add";
                this.disk = {
                    ime : "",
                    tip : "",
                    kapacitet : "",
                    organizacija: null,
                    vm: null
                };
            } else {
                this.type = "change";
                this.disk = model;
            }

        },

        add : function() {
            var self = this;
            axios
            .post("rest/Disks/", self.disk)
            .then(response => {
                this.$emit("add", response.data);
            })
            .catch(function(error) { alert("Disk sa unetim imenom vec postoji!")});
            $("#diskModal").modal('hide');
        },

        change : function() {
            var self = this;
            axios
            .post("rest/Disks/" + self.disk.ime, self.disk)
            .then(response => {
                this.$emit("change", response.data);
            });
            $("#diskModal").modal('hide');
        },

        deleteDisk : function() {
            var self = this;
            axios
            .delete("rest/Disks/" + self.disk.ime)
            .then(response => {
                this.$emit("deleteDisk", response.data);
            })
            .catch(function(error) {
                alert("Neuspesno brisanje diska! Disk je u upotrebi!");
            });
            $("#diskModal").modal('hide');
        }

    }


});