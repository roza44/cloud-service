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
            this.$router.push("/openOrg/" + org.ime);
        }
    },

    mounted () {
        this.pullOrgs();
    }

});