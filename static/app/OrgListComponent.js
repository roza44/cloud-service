Vue.component("org-list", {

    data: function() {
        return {
            role : ""
        }
    },

    template: `
        
    `
    ,

    mounted () {
        axios
        .get("rest/Users/info")
        .then(response => {this.role = response.data;})
        .catch(function(error){alert(error);});
    }

});