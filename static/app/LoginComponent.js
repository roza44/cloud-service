Vue.component("login", {
    
    data: function() {
        return {
            username : "",
            password : ""
        }
    },

    template: `
    <div>
        <table>
            <tr>
                <td>Username</td>
                <td><input type="text" v-model="username"></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" v-model="password"></td>
            </tr>
            <tr>
                <td></td>
                <td><button v-on:click="login">Uloguj se</button></td>
            </tr>
        </table>
    </div>
    `
    ,

    methods: {
        login : function() {
            if(this.username == "" || this.password == "")
                alert("Invalid input!");
            else {
                //Koriscenje this u .then pozivu nije preporucljivo i zato kreiramo self
                self = this;
                axios
                .post("rest/Users/login", {"username": this.username, "password": this.password})
                .then(response => (this.redirect(response)))
                .catch(function(error){alert(error)});
            }
        },

        redirect : function(response) {
            if(response.data == "OK")
                this.$router.push("/dashboard")
            else
                alert(response.data);
        }
    }


});