Vue.component("user_modal", {

    data : function() {

        return {
            orgIds : [],
            type : "",
            email : "",
            name : "",
            secondname : "",
            password : "",
            selRole : "",
            selOrg : "",
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
                        <label for="select1">Uloga</label>
                        <select id="select1" v-model="selRole" class="form-control">
                            <option value="user">User</option>
                            <option value="admin">Admin</option>
                        </select>
                    </div>
                    <div v-if="role==='superadmin'" class="form-group">
                        <label for="select2">Organizacija</label>
                        <select id="select2" v-model="selOrg" class="form-control">
                            <option v-for="o in orgIds" :value="o">{{o}}</option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button v-if="type==='add'" v-on:click="add" type="button" class="btn btn-primary">Dodaj</button>
                <button v-if="type==='change'" v-on:click="change" type="button" class="btn btn-primary">Izmeni</button>
                <button v-if="type==='change'" v-on:click="deleteUser" type="button" class="btn btn-warning">
                    Izbrisi korisnika
                </button>
            </div>
            </div>
        </div>
    </div>
    
    `,

    methods: {
        //Prikazivanje modala u zavisnosti od modela
        show : function(model) {
            this.fillContent(model);
            $("#exampleModal").modal('show');
        },

        //Popunjavanje polja modala. Ukoliko nema modela za prikaz postavljaju se default polja
        fillContent : function(model) {
            if(model == null) {
                //Ukoliko je korisnik admin on nema opciju izbora organizacije, vec se smatra da
                //se novi korisnik dodaje u organizaciju admina
                var org = (this.role==='admin')? "None": this.orgIds[0];
                this.setData("", "", "", "", "user", org, 'add');
            }
            else
            {
                this.setData(model.email, model.ime, model.prezime, model.lozinka,
                             model.uloga, model.organizacija.ime, 'change');
            }
        },

        setData  : function(email, name, secondname, password, selRole, selOrg, type)
        {
            this.email = email;
            this.name = name;
            this.secondname = secondname;
            this.password = password;
            this.selRole = selRole;
            this.selOrg = selOrg;
            this.type = type;
        },

        //dodavanje novog korisnika
        add : function() {

            if(!this.validate() || this.email === "")
                alert("Nevalidan unos! Sva polja moraju biti popunjena!");
            else {
                var self = this;
                axios
                .post("rest/Users/addUser/" + self.selOrg, {"email" : self.email, "ime" : self.name, "prezime" : self.secondname,"lozinka" : this.password, "uloga" : this.selRole})
                .then(response => {
                    this.$emit("addUser", response.data);
                })
                .catch(function(error) { alert(error.response.data)});
                $("#exampleModal").modal('hide');
            }
        },

        //izmena korisnika
        change : function() {
            if(!this.validate() || this.email === "")
                alert("Nevalidna izmena! Sva polja moraju biti popunjena!");
            else {
                var self = this;
                axios
                .post("rest/Users/changeUser/" + self.selOrg, {"email" : self.email, "ime" : self.name, "prezime" : self.secondname,"lozinka" : this.password, "uloga" : this.selRole})
                .then(response => {
                    this.$emit("changeUser", response.data);
                });
                $("#exampleModal").modal('hide');
            }

        },

        //brisanje korisnika
        deleteUser : function() {
            var self = this;
            axios
            .post("rest/Users/deleteUser/" + this.email)
            .then(response => {
                this.$emit("deleteUser", response.data);
            })
            .catch(function(error) {
                alert(error.response.data);
            });
            $("#exampleModal").modal('hide');
        },

        //validacija polja modala
        validate : function() {
            if(this.name === "") return false;
            if(this.secondname === "") return false;
            if(this.password === "") return false;
            return true;
        }

    },

    mounted () {
        this.role = localStorage.getItem('role');

        var self = this;
        if(this.role === 'superadmin') { //dobavi id listu organizacija ukuliko je korisnik koji dodaje super admin
            axios
            .get("rest/Organizations/getIds")
            .then( response => { 
                self.orgIds = response.data; 
            })
            .catch( function(error) {
                alert(error.response.data);
            });
        }
    }


});