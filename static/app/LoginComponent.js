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
                <td><input type="text" v-model="username"></td>
            </tr>
            <tr>
                <td><input type="text" v-model="password"></td>
            </tr>
            <tr>
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
                .post("rest/login", {"email": this.usernme, "lozinka": this.password})
                .then(response => (alert(response.data)));
            }
        }
    }


});