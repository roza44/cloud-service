Vue.component("cat_modal", {
    data : function() {

        return {
            type : "",
            name : "",
            cpu : "",
            ram : "",
            gpu : ""
        }

    },

    template : `
    <div class="modal fade" id="catModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header">
                <h5 v-if="type==='add'" class="modal-title" id="exampleModalLabel">Dodavanje kategorije</h5>
                <h5 v-if="type==='change'" class="modal-title" id="exampleModalLabel">Izmena kategorije: {{name}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div v-if="type==='add'" class="form-group">
                        <label for="formGroupExampleInput1">Ime</label>
                        <input type="text" class="form-control" id="formGroupExampleInput1" v-model="name">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput2">Broj CPU jezgara</label>
                        <input type="text" class="form-control" id="formGroupExampleInput2" v-model="cpu">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput3">RAM</label>
                        <input type="text" class="form-control" id="formGroupExampleInput3" v-model="ram">
                    </div>
                    <div class="form-group">
                        <label for="formGroupExampleInput4">Broj GPU jezgara</label>
                        <input type="text" class="form-control" id="formGroupExampleInput4" v-model="gpu">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button v-if="type==='add'" v-on:click="add" type="button" class="btn btn-primary">Dodaj</button>
                <button v-if="type==='change'" v-on:click="change" type="button" class="btn btn-primary">Izmeni</button>
                <button v-if="type==='change'" v-on:click="deleteCat" type="button" class="btn btn-primary" style="background-color:coral">
                    Izbrisi kategoriju
                </button>
            </div>
            </div>
        </div>
    </div>
    
    `,

    methods: {
        show : function(model) {
            this.fillContent(model);
            $("#catModal").modal('show');
        },

        fillContent : function(model) {
            if(model == null)
                this.setData("", "", "", "", 'add');
            else
                this.setData(model.ime, model.brojJezgara, model.ram, model.gpuJezgra, 'change');
        },

        setData  : function(name, cpu, ram, gpu, type)
        {
            this.name = name;
            this.cpu = '' + cpu;
            this.ram = '' + ram;
            this.gpu = '' + gpu;
            this.type = type;
        },

        add : function() {
            if(!this.validate())
                alert("Nevalidan unos! Sva polja moraju biti popunjena!");
            else {
                var self = this;
                axios
                .post("rest/Categories/addCat", {"ime" : self.name, "brojJezgara" : parseInt(self.cpu), "ram" : parseFloat(self.ram), "gpuJezgra" : parseInt(self.gpu)})
                .then(response => {
                    this.$emit("addCat", response.data);
                })
                .catch(function(error) { alert("Kategorija sa unetim imenom vec postoji!")});
                $("#catModal").modal('hide');
            }
        },

        change : function() {
            if(!this.validate())
                alert("Nevalidna izmena! Sva polja moraju biti popunjena!");
            else {
                var self = this;
                axios
                .post("rest/Categories/changeCat", {"ime" : self.name, "brojJezgara" : parseInt(self.cpu), "ram" : parseFloat(self.ram), "gpuJezgra" : parseInt(self.gpu)})
                .then(response => {
                    this.$emit("changeCat", response.data);
                });
                $("#catModal").modal('hide');
            }

        },

        deleteCat : function() {
            var self = this;
            axios
            .post("rest/Categories/deleteCat", {"ime" : self.name, "brojJezgara" : parseInt(self.cpu), "ram" : parseFloat(self.ram), "gpuJezgra" : parseInt(self.gpu)})
            .then(response => {
                this.$emit("deleteCat", response.data);
            })
            .catch(function(error) {
                alert("Neuspesno brisanje kategorije! Kategorija je u upotrebi!");
            });
            $("#catModal").modal('hide');
        },

        validate : function() {
            if(this.name === "") return false;
            if(this.cpu === "") return false;
            if(this.gpu === "") return false;
            if(this.ram === "") return false;
            return true;
        }

    },


});