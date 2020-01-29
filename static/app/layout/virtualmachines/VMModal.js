Vue.component("vm_modal", {

    data : function() {
        return {
            //mounted values
            role : "",
            catList : [],
            orgList : [],
            discList : [],

            //modal values
            name : "",
            selCat : "",
            selOrg : "",
            active : false,
            type : ""
        };
    },

    template : `
        <div class="modal fade" id="mainModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                    <h5 v-if="type === 'add'" class="modal-title" id="exampleModalLabel">Dodavanje virtualnih masina</h5>
                    <h5 v-if="type === 'change'"class="modal-title" id="exampleModalLabel">Izmena virualne masine: {{name}}</h5>
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
                            <select v-model="selCat" id="selectVM1" class="form-control">
                                <option v-for="c in catList" :value="c">{{c}}</option>
                            </select>
                        </div>
                        <div v-if="role==='superadmin' && type==='add'" class="form-group">
                            <label for="selectVM2">Organizacija</label>
                            <select @change="updateDiscs($event)" v-model="selOrg" id="selectVM2" class="form-control">
                                <option v-for="o in orgList" :value="o">{{o}}</option>
                            </select>
                        </div>
                        <div>
                            <select id="discSelect" class="mdb-select md-form form-control" multiple  style="width:450px" >
                                <option v-for="d in discList">{{d}}</option>
                            </select>
                        </div>
                        <div>
                            <br>
                            <label>Aktivnost: <input type="checkbox" v-model="active"></input></label>
                        </div>
                    </form>
                </div>
                <div v-if="type === 'add'" class="modal-footer">
                    <button v-on:click="addVm" type="button" class="btn btn-primary">Dodaj</button>
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
                this.setData("", this.catList[0], org, 'add');
        },

        setData : function(name, selCat, selOrg, type) {
            this.name = name;
            this.selCat = selCat;
            this.selOrg = selOrg;
            this.getDiscs(selOrg);
            this.type = type;
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