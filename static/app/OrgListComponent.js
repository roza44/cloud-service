Vue.component("org-list", {

    data: function() {
        return {
            organizations : [],
            editingName : "",
            editingOrg : null
        }
    },

    template: `
    <div>
        <h3>Sve organizacije u sistemu</h3>
        <table>
            <tr>
                <th>Ime</th>
                <th>Opis</th>
            </tr>

            <tr v-for="org in organizations">
                <td>
                    <div v-if="editingOrg === org">
                        <input @keyup.enter="editOrg(null)" type="text" v-model="org.ime">
                    </div>

                    <div v-else>
                        <input class="grayInput" @click="editOrg(org)" type="text" v-model="org.ime" readonly>
                    </div>
                </td>
                <td>
                    <div v-if="editingOrg === org">
                        <textarea @keyup.enter="editOrg(null)" rows="4" cols="50" v-model="org.opis">{{org.opis}}</textarea>
                    </div>

                    <div v-else>
                        <textarea class="grayInput" @click="editOrg(org)" rows="4" cols="50" v-model="org.opis" readonly>{{org.opis}}</textarea>
                    </div>
                    
                    
                </td>
            </tr>
        </table>

        <table>
            <tr>
                <button v-if="editingOrg === null" @click="addOrg()">Dodaj</button>
                <button v-else @click="addOrg()" disabled>Dodaj</button>
                <br><br>
            </tr>
            <tr>
                <td>
                    <button @click="pullOrgs()">Poništi</button>
                </td>
            </tr>
        </table>
    </div>
    `
    ,

    methods: {
        pullOrgs: function() {
            this.editingOrg = null;

            axios
            .get("rest/Organizations")
            .then(response => {
                this.organizations = response.data;
            })
            .catch(function(error){alert(error);});
        },

        addOrg: function() {
            org = {ime: "", opis: ""};
            this.organizations.push(org);

            this.editOrg(org);
        },

        editOrg: function(org) {
            // If no org is currently selected, just switch
            if (this.editingOrg == null) {
                this.editingOrg = org;
                this.editingName = org.ime;
                return;
            }

            if (org !== null && this.editingName === "") {
                // In the case of adding a new org
                this.editingName = org.ime;
            }

            // Update edited organization
            axios
            .post("rest/Organizations/" + this.editingName, this.editingOrg)
            .then(response => {
                // Izmene uspesne, aktiviraj sledeci
                this.editingOrg = org;

                if (org !== null) {
                    this.editingName = org.ime;
                }
            })
            .catch(function(error){alert(error);});
            
        }
    },

    mounted () {
        this.pullOrgs();
    }

});