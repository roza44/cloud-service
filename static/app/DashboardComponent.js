Vue.component("dashboard", {

    data: function() {
        return {
            role : ""
        }
    },

    template: `
    <div v-if="role === 'superadmin'">
        <!--Listanje organizacije-->
        <org-list></org-list>
        <button>Dodaj organizaciju</button>
    </div>

    <div v-if="role === 'admin'">
        
    </div>

    <div v-if="role === 'user'">

    </div>
    `
    ,

    mounted () {
        axios
        .get("rest/Users/info")
        .then(response => {this.role = response.data;})
        .catch(function(error){alert(error);});
    }

});