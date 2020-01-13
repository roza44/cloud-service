Vue.component("dashboard", {

    data: function() {
        return {
            role : ""
        }
    },

    template: `
        <div>
            <p>Showing {{role}} dashboard</p>

            <div v-if="role === 'admin'">
                Ovo admin sme da vidi
            </div>
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