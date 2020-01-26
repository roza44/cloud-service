Vue.component("user_modal", {

    data : function() {

        return {
            type : "",
            email : "",
            name : "",
            secondname : "",
            password : "",
            role : ""
        }

    },

    template : `
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 v-if="type==='add'" class="modal-title" id="exampleModalLabel">Dodavanje korisnika</h5>
                <h5 v-if="type==='change'" class="modal-title" id="exampleModalLabel">Izmena korisnika: {{email}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div v-if="type==='add'" class="form-group">
                        <label for="formGroupExampleInput1">Email</label>
                        <input type="text" class="form-control" id="formGroupExampleInput1" v-model="email">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput2">Lozinka</label>
                        <input type="text" class="form-control" id="formGroupExampleInput2" v-model="password">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput3">Ime</label>
                        <input type="text" class="form-control" id="formGroupExampleInput3" v-model="name">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput4">Prezime</label>
                        <input type="text" class="form-control" id="formGroupExampleInput4" v-model="secondname">
                    </div>
                    <div class="form-group">
                        <label for="roleSel">Uloga</label>
                        <select ref="select" id="roleSel" class="form-control">
                            <option value="user">User</option>
                            <option value="admin">Admin</option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button v-if="type==='add'" v-on:click="add" type="button" class="btn btn-primary">Dodaj</button>
                <button v-if="type==='change'" v-on:click="change" type="button" class="btn btn-primary">Izmeni</button>
                <button v-if="type==='change'" v-on:click="deleteUser" type="button" class="btn btn-primary" style="background-color:coral">
                    Izbrisi korisnika
                </button>
            </div>
            </div>
        </div>
    </div>
    
    `,

    methods: {
        show : function(model) {
            this.fillContent(model);
            $("#exampleModal").modal('show');
        },

        fillContent : function(model) {
            if(model == null)
                this.setData("", "", "", "", null, 'add');
            else
            {
                this.setData(model.email, model.ime, model.prezime, model.lozinka, model.uloga, 'change');
            }
        },

        setData  : function(email, name, secondname, password, selRole, type)
        {
            this.email = email;
            this.name = name;
            this.secondname = secondname;
            this.password = password;
            this.$refs.select.selectedIndex = (selRole === "admin")? 1:0;
            this.type = type;
        },

        add : function() {

            if(!this.validate() || this.email === "")
                alert("Nevalidan unos! Sva polja moraju biti popunjena!");
            else {
                var self = this;
                axios
                .post("rest/Users/addUser", {"email" : self.email, "ime" : self.name, "prezime" : self.secondname,
                                             "lozinka" : this.password, "uloga" : this.$refs.select.value})
                .then(response => {
                    this.$emit("addUser", response.data);
                })
                .catch(function(error) { alert("Korisnik sa unetim email-om vec postoji!")});
                $("#exampleModal").modal('hide');
            }
        },

        change : function() {
            if(!this.validate() || this.email === "")
                alert("Nevalidna izmena! Sva polja moraju biti popunjena!");
            else {
                var self = this;
                axios
                .post("rest/Users/changeUser", {"email" : self.email, "ime" : self.name, "prezime" : self.secondname, "lozinka" : this.password})
                .then(response => {
                    this.$emit("changeUser", response.data);
                });
                $("#exampleModal").modal('hide');
            }

        },

        deleteUser : function() {
            var self = this;
            axios
            .post("rest/Users/deleteUser", {"email" : self.email, "ime" : self.name, "prezime" : self.secondname, "lozinka" : this.password})
            .then(response => {
                this.$emit("deleteUser", response.data);
            })
            .catch(function(error) {
                alert("Neuspesno brisanje korisnika!");
            });
            $("#exampleModal").modal('hide');
        },

        validate : function() {
            if(this.name === "") return false;
            if(this.secondname === "") return false;
            if(this.password === "") return false;
            return true;
        }

    },

    mounted () {
        this.role = localStorage.getItem('role');
    }


});