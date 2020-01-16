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
            <tr v-for="org in organizations">
                <table>
                    <tr>
                        <td>
                            <div v-if="editingOrg === org">
                                <input @keydown.enter.prevent="editOrg(null)" type="text" v-model="org.ime">
                            </div>

                            <div v-else>
                                <input class="grayInput" @click="editOrg(org)" type="text" v-model="org.ime" readonly>
                            </div>
                        </td>

                        <td>
                            <div v-if="editingOrg === org">
                                <textarea @keydown.enter.prevent="editOrg(null)" rows="4" cols="50" v-model="org.opis">{{org.opis}}</textarea>
                            </div>

                            <div v-else>
                                <textarea class="grayInput" @click="editOrg(org)" rows="4" cols="50" v-model="org.opis" readonly>{{org.opis}}</textarea>
                            </div>
                            
                            <div v-if="editingOrg === org">
                                <button @click="editOrg(null)">Potvrdi</button>
                                <button @click="pullOrgs()">Poništi</button>
                            </div>
                        </td>
                    </tr>
                </table>
            </tr>

            <tr>
                <button class="fullRect" v-if="editingOrg === null" @click="addOrg()">Dodaj</button>
                <button class="fullRect" v-else @click="addOrg()" disabled>Dodaj</button>
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

        switchOrgFocus: function(org) {
            this.editingOrg = org;

            if (org !== null) {
                this.editingName = org.ime;
            } else {
                this.editingName = "";
            }
        },

        editOrg: function(org) {
            // If no org is currently selected, just switch
            if (this.editingOrg == null) {
                this.switchOrgFocus(org);
                return;
            }
            
            // Check for invalid input
            if (this.editingOrg.ime === "" || this.editingOrg.opis === "") {
                alert("Ime i opis organizacije ne smeju ostati prazni!");
                return;
            }

            // Update edited organization
            axios
            // If it's a new org, posts straight to Organizations/, else posts to update route with previous name
            .post("rest/Organizations/" + this.editingName, this.editingOrg)
            .then(response => {
                // Update successful, switch to next field
                this.switchOrgFocus(org);
            })
            .catch(function(error){alert(error);});
            
        }
    },

    mounted () {
        this.pullOrgs();
    }

});