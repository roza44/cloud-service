Vue.component("vm_modal", {

    data : function() {
        return {
            //mounted values
            role : "",
            catList : [],
            orgList : [],
            discList : [],
            selDiscs : [],

            //modal values
            name : "",
            selCat : "",
            selOrg : "",
            active : false,
            activities : [],
            type : ""
        };
    },

    template : `
        <div class="modal fade" id="mainModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                    <h5 v-if="type === 'add'" class="modal-title" id="exampleModalLabel">Dodavanje virtualnih masina</h5>
                    <h5 v-if="type === 'change' && role !== 'user'"class="modal-title" id="exampleModalLabel">Izmena virualne masine: {{name}}</h5>
                    <h5 v-if="role === 'user'" class="modal-title" id="exampleModalLabel">Pregled virualne masine: {{name}}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div v-if="type === 'add'" class="form-group">
                            <label for="inputVM1">Ime</label>
                            <input v-model="name" id="inputVM1" type="text" class="form-control">
                        </div>
                        <div class="form-group">
                            <label for="selectVM1">Kategorija</label>
                            <select v-model="selCat" id="selectVM1" class="form-control" :disabled="role === 'user'">
                                <option v-for="c in catList" :value="c">{{c}}</option>
                            </select>
                        </div>
                        <div v-if="role==='superadmin' && type==='add'" class="form-group">
                            <label for="selectVM2">Organizacija</label>
                            <select @change="updateDiscs($event)" v-model="selOrg" id="selectVM2" class="form-control">
                                <option v-for="o in orgList" :value="o">{{o}}</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="discSelect">Diskovi</label>
                            <select id="discSelect" class="mdb-select md-form form-control" multiple  style="width:450px" >
                                <option v-for="sd in selDiscs" selected :disabled="role === 'user'" :value="sd" :id="sd">{{sd}}</option>
                                <option v-if="role !== 'user'" v-for="d in discList" :value="d" :id="d">{{d}}</option>
                            </select>
                        </div>
                        <div v-if="type === 'change' && role !== 'user'" class="form-group">
                            <label for="activities">Aktivnosti</label>
                            <select id="activities" class="mdb-select md-form form-control" multiple  style="width:450px" >
                                <option v-for="a in activities" selected :value="sd" :id="sd" disabled>
                                {{a.timestamp}}: {{a.turnedOn}}
                                </option>
                            </select>
                        </div>
                        <div class="form-group">
                            <br>
                            <label>Aktivnost: <input type="checkbox" v-model="active" :disabled="role === 'user'"></input></label>
                        </div>
                    </form>
                </div>
                <div v-if="type === 'add'" class="modal-footer">
                    <button v-on:click="addVm" type="button" class="btn btn-primary">Dodaj</button>
                </div>
                <div v-if="type === 'change' && role !== 'user'" class="modal-footer">
                    <button v-on:click="changeVM" type="button" class="btn btn-primary">Izmeni</button>
                    <button v-on:click="deleteVM" type="button" class="btn btn-warning">Izbrisi virtualnu masinu</button>
                </div>
            </div>
            </div>
        </div>
    `,

    methods : {

        show : function(model) {
            this.fillContent(model);
            $("#mainModal").modal('show');
        },

        fillContent : function(model) {
            var org = (this.role==='superadmin')? this.orgList[0]:"None";
            if(model === null)
                this.setData("", this.catList[0], org, [],false, 'add');
            else {
                var index = model.aktivnosti.length - 1;
                var a;
                if(index === -1) a = false;
                else a = model.aktivnosti[index].turnedOn;
                this.setData(model.ime, model.kategorija.ime, model.organizacija.ime, model.aktivnosti, a, 'change');
            }
        },

        setData : function(name, selCat, selOrg, activities, active, type) {
            this.name = name;
            this.selCat = selCat;
            this.selOrg = selOrg;
            this.activities = activities;
            this.active =active;
            this.type = type;
            
            if(this.role !== 'user')
                this.resetMulitselect();

            this.getDiscs(selOrg);
            if(this.type === 'change')
                this.getVmDiscs(this.name);
            else
                this.selDiscs = [];
        },

        resetMulitselect : function() {
            this.discList.forEach(element => {
                document.getElementById(element).selected = false;
            });

            this.selDiscs.forEach(element => {
                document.getElementById(element).selected = true;
            });

        },

        updateDiscs : function(event) {
            this.getDiscs(event.target.value);
        },

        addVm : function() {

            if(this.validate()) {
                var self = this;
                axios
                .post("rest/VirualMachines/addVM", this.formToJSON())
                .then(response => {
                    this.$emit("addVM", response.data);
                    $("#mainModal").modal('hide');
                })
                .catch(function(error) {
                    alert("Virutalna masina sa zadatim imenom vec postoji!");
                });
            }
            else  
                alert("Nevalidan unos! Sva polja moraju biti popunjena!");

        },

        changeVM : function() {
            if(this.validate()) {
                axios
                .post("rest/VirualMachines/changeVM", this.formToJSON())
                .then(response => {
                    this.$emit("changeList", response.data);
                    $("#mainModal").modal('hide');
                })
                .catch(function(error) {
                    alert("Neuspesna izmena!");
                });
            }
            else
                alert("Nevalidan unos! Sva polja moraju biti popunjena!");
        },

        deleteVM : function() {
            var self = this;
            axios
            .delete("rest/VirualMachines/deleteVM/" + self.name)
            .then(response => {
                this.$emit("deleteList", response.data);
            });
        },

        formToJSON : function() {
            var discs = $("#discSelect").val();
                if(discs === null)
                    discs = [];
            return {"ime" : this.name, "kategorija" : this.selCat, "organizacija" : this.selOrg, "diskovi" : discs, "aktivnost" : this.active};
        },

        getCats : function() {
            var self = this;
            axios
            .get("rest/Categories/getIds")
            .then(response => {
                self.catList = response.data;
            });
        },

        getOrgs : function() {
            var self = this;
            axios
            .get("rest/Organizations/getIds")
            .then(response => {
                self.orgList = response.data;
            });
        },

        getDiscs : function(discSource) {
            var self = this;
            axios
            .get("rest/Disks/getIds/" + discSource)
            .then(response => {
                this.discList = response.data;
            });
        },

        getVmDiscs : function(vm) {
            var self = this;
            axios
            .get("rest/VirualMachines/getDiscsIds/" + vm)
            .then(response => {
                this.selDiscs = response.data;
            });
        },
         
        validate : function() {
            if(this.name === "") return false;
            return true;
        }
    },

    mounted() {

        this.role = localStorage.getItem('role');
        this.getCats();
        if(this.role === 'superadmin') {
            this.getOrgs();
        }
    }

});