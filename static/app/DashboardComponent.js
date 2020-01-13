Vue.component("dashboard", {

    data: function() {
        return {
            role : ""
        }
    },

    template: `
        <div>
            <p>{{role}}</p>
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