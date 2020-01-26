Vue.component("org_list", {

    data: function() {
        return {
            organizations : []
        }
    },

    template: `
    <div>
        <h2>Organizacije</h2>
        Kliknite na red u tabeli da izmenite polja te organizacije.
        <table class="table table-striped table-hover">
        <thead>
            <tr>
                <th>Slika</th>
                <th>Ime</th>
                <th>Opis</th>
            </tr>
        </thead>
        <tbody v-for="org in organizations">
            <tr @click="editOrg(org)">
                <td><img :src="'data/' + org.slikaPutanja" height="64"></td>
                <td>{{org.ime}}</td>
                <td>{{org.opis}}</td>
            </tr>
        </tbody>
        </table>

        <button type="button" class="btn btn-outline-primary btn-lg btn-block" @click="editOrg(null)">
        Dodaj organizaciju
        </button>

        <org_modal ref="orgModal" @editOrg="orgEdited($event)" @addOrg="orgAdded($event)">
        </org_modal>
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

        editOrg: function(org) {
            // Show modal
            this.$refs.orgModal.show(org);
        },

        orgAdded : function(org) {
            this.organizations.push(org);
        },
          
        orgEdited : function(org) {
            for(i=0; i < this.organizations.length; i++) {
                if(this.organizations[i].ime === org.ime) {
                    this.organizations.splice(i, 1, org);
                    break;
                }
            }
        }
    },

    mounted () {
        this.pullOrgs();
    }

});