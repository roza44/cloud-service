Vue.component("open_organization", {
    data: function() {
        return {
            org : {
                ime: "",
                opis: ""
            },
            slikaFile : null
        };
    },

    template: `
        <default_layout>
            <div class="container">
                <div v-if="$route.params.ime" class="row">
                    <h2>Izmena organizacije</h2>
                </div>
            
                <div v-else class="row">
                    <h2>Dodavanje nove organizacije</h2>
                </div>
            
                <div class="row">
                    <form>
                        <div class="form-group">
                            <label for="ime">Ime</label>
                            <input v-if="$route.params.ime" type="text" class="form-control" id="ime" v-model="org.ime" readonly>
                            <input v-else type="text" class="form-control" v-model="org.ime" id="ime">
                        </div>
                        <div class="form-group">
                            <label for="opis">Opis</label>
                            <textarea class="form-control" id="opis" rows="3" v-model="org.opis"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="slikaPolje">Slika</label>
                            <input type="file" class="form-control-file" id="slikaPolje" ref="slikaPolje" @change="handleFileChange()">
                        </div>
                        <button v-if="$route.params.ime" type="submit" class="btn btn-primary" @click="editOrg(org.ime)">Izmeni</button>
                        <button v-else type="submit" class="btn btn-primary" @click="editOrg(null)">Dodaj</button>
                    </form>
                </div>
            </div>
        </default_layout>
    `,

    methods: {
        handleFileChange: function() {
            this.slikaFile = this.$refs.slikaPolje.files[0];
        },
        editOrg: function(orgName) {
            self = this;
            let path = 'rest/Organizations/';
            if (orgName !== null) {
                path = path + orgName
            }
            axios.post(path, self.org)
            .then(function(){
                // Image
                if (self.slikaFile) {
                    // Upload image
                    axios.post( '/rest/setOrgImage/' + self.org.ime + "/" + self.slikaFile.name, self.slikaFile)
                    .then(function(){
                        self.slikaFile = null;
                        self.$router.push("/organizations");
                    })
                    .catch(function(){
                        console.log('Failed to upload pic!');
                    });
                } else {
                    self.$router.push("/organizations");
                }
            })
            .catch(function(error) {alert(error);});

        },
    },
    mounted () {
        if (this.$route.params.ime) { // Ako menjamo vec postojecu, ucitaj je
            axios
            .get("rest/Organizations/" + this.$route.params.ime)
            .then(response => {
                this.org = response.data;
            })
            .catch(function(error){alert(error);});
        }
    }

});