Vue.component("disk_modal", {
    data : function() {

        return {
            disk : {
                ime : "",
                tip : "",
                kapacitet : ""
            },

            type : null
        }

    },

    template : `
    <div class="modal fade" id="diskModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 v-if="type==='add'" class="modal-title" id="exampleModalLabel">Dodavanje diska</h5>
                <h5 v-if="type==='change'" class="modal-title" id="exampleModalLabel">Izmena diska: {{disk.ime}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div v-if="type==='add'" class="form-group">
                        <label for="formGroupExampleInput1">Ime</label>
                        <input type="text" class="form-control" id="formGroupExampleInput1" v-model="disk.ime">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput2">Tip</label>
                        <input type="text" class="form-control" id="formGroupExampleInput2" v-model="disk.tip">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput3">Kapacitet</label>
                        <input type="text" class="form-control" id="formGroupExampleInput3" v-model="disk.kapacitet">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button v-if="type==='add'" v-on:click="add" type="button" class="btn btn-primary">Dodaj</button>
                <button v-if="type==='change'" v-on:click="change" type="button" class="btn btn-primary">Izmeni</button>
                <button v-if="type==='change'" v-on:click="deleteDisk" type="button" class="btn btn-primary" style="background-color:coral">
                    Izbrisi disk
                </button>
            </div>
            </div>
        </div>
    </div>
    
    `,

    methods: {
        show : function(model) {
            this.fillContent(model);
            $("#diskModal").modal('show');
        },

        fillContent : function(model) {
            if(model == null) {
                this.type = "add";
                this.disk = {
                    ime : "",
                    tip : "",
                    kapacitet : ""
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