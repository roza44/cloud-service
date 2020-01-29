Vue.component("org_modal", {
    data : function() {
        return {
            org : {
                ime: "",
                opis: ""
            },
            slikaFile : null,
            type : "add"
        }
    },

    template : `
    <div class="modal fade" id="orgModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 v-if="type==='add'" class="modal-title" id="exampleModalLabel">Dodavanje organizacije</h5>
                    <h5 v-if="type==='edit'" class="modal-title" id="exampleModalLabel">Izmena organizacije: {{org.ime}}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div v-if="type==='add'" class="form-group">
                            <label for="input_add">Ime</label>
                            <input type="text" class="form-control" id="input_add" v-model="org.ime">
                        </div>
                        <div class="form-group">
                            <label for="txt_opis">Opis</label>
                            <textarea class="form-control" id="txt_opis" rows="3" v-model="org.opis"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="slikaPolje">Slika</label>
                            <input type="file" class="form-control-file" id="slikaPolje" ref="slikaPolje" @change="handleFileChange()">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button v-if="type==='add'" @click="editOrg(null)" type="button" class="btn btn-primary">Dodaj</button>
                    <button v-if="type==='edit'" @click="editOrg(org.ime)" type="button" class="btn btn-primary">Izmeni</button>
                </div>
            </div>
        </div>
    </div>
    `,

    methods: {
        show: function(org) {
            if (org === null) {
                this.org.ime = "";
                this.org.opis = "";
                this.type = "add";
            } else {
                this.org.ime = org.ime;
                this.org.opis = org.opis;
                this.type = "edit";
            }
            $("#orgModal").modal('show');
        },

        handleFileChange: function() {
            this.slikaFile = this.$refs.slikaPolje.files[0];
        },

        emitEventAndClose: function(org) {
            this.$emit(this.type + "Org", org);
            $("#orgModal").modal('hide');
        },

        editOrg: function(orgName) {
            self = this;
            let path = 'rest/Organizations/';
            if (orgName !== null) {
                path = path + orgName
            }
            
            if(this.validateInput()) {
                // Send the request
                axios.post(path, self.org)
                .then(response => {
                    // Image
                    if (self.slikaFile) {
                        // Upload image
                        axios.post( '/rest/setOrgImage/' + self.org.ime + "/" + self.slikaFile.name, self.slikaFile)
                        .then(res => {
                            self.slikaFile = null;
                            self.emitEventAndClose(res.data);
                        })
                        .catch(function(){
                            console.log('Failed to upload pic!');
                        });
                    } else {
                        self.emitEventAndClose(response.data);
                    }
                })
                .catch(function(error) {alert(error);});
            }
        },

        validateInput: function() {
            if (this.org.ime === "") {
                alert("Ime organizacije je obavezno polje!");
                return false;
            }

            return true;
        }
    }
});