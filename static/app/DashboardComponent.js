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
        </div>

        <div v-else-if="role === 'admin'">
            
        </div>

        <div v-else-if="role === 'user'">

        </div>
    `
    ,

    methods: {
        logout : function () {
            axios
            .get("rest/Users/logut")
            .then(response  => {
                this.$router.push("/");
            })
            .cathc(function(error){alert(error);})
        }

    },

    mounted () {
        axios
        .get("rest/Users/info")
        .then(response => {this.role = response.data;})
        .catch(function(error){alert(error);});
    }

});