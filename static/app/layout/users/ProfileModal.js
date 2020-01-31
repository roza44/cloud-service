Vue.component("profile_modal", {

    data: function() {
        return {
            user : {
                email : "",
                ime : "",
                prezime : "",
                lozinka : ""
            }
        }
    },

    template : `
        <div class="modal fade" id="profileModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                    <h5 class="modal-title" id="profileModalTitle">Izmena profila</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="input1">Ime</label>
                            <input type="text" class="form-control" id="input1" v-model="user.ime">
                        </div>
                        <div class="form-group">
                            <label for="input2">Prezime</label>
                            <input type="text" class="form-control" id="input2" v-model="user.prezime">
                        </div>
                        <div class="form-group">
                            <label for="input3">Lozinka</label>
                            <input type="text" class="form-control" id="input3" v-model="user.lozinka">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button v-on:click="changeProfile" type="button" class="btn btn-primary">Izmeni</button>
                </div>
                </div>
            </div>
        </div>
    `,

    methods : {
        show : function(model) {
            this.user.email = model.email;
            this.user.ime = model.ime;
            this.user.prezime = model.prezime;
            this.user.lozinka = model.lozinka;
            $("#profileModal").modal('show');
        },

        changeProfile : function() {
            if(this.validate()) {
                var self = this;
                axios
                .post("rest/Users/updateProfile", {"email" : self.user.email,"ime" : self.user.ime,
                                                   "prezime" : self.user.prezime, "lozinka" : self.user.lozinka})
                .then(response => {
                    this.$emit("changeProfile", response.data);
                    $("#profileModal").modal('hide');
                });
            }
            else
                alert("Nevalidan unos! Sva polja moraju biti popunjena!");

        },

        validate : function() {
            if(this.user.ime === "") return false;
            if(this.user.prezime === "") return false;
            if(this.user.lozinka === "") return false;
            return true;
        }
    }


});
