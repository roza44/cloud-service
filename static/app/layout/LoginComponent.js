Vue.component("login", {
    
    data: function() {
        return {
            username : "",
            password : ""
        }
    },

    template: `
    <body class="text-center">
    <form class="form-signin">
      <img class="mb-4" src="../data/empty_cloud.png" alt="" width="120" height="120">
      <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
      <label for="inputEmail" class="sr-only">Email address</label>
      <input v-model="username" type="email" id="inputEmail" class="form-control" placeholder="Email address" required="" autofocus="">
      <label for="inputPassword" class="sr-only">Password</label>
      <input v-model="password" type="password" id="inputPassword" class="form-control" placeholder="Password" required="">
      <button v-on:click="login" class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      <p class="mt-5 mb-3 text-muted">© 2019-2020</p>
    </form>
    </body>
    `
    ,

    methods: {
        login : function() {
            var self = this;
            if(this.username == "" || this.password == "")
                alert("Invalid input!");
            else {
                //Koriscenje this u .then pozivu nije preporucljivo i zato kreiramo self
                self = this;
                axios
                .post("rest/login", {"username": this.username, "password": this.password})
                .then(response => {self.$router.push("/dashboard")})
                .catch(function(error){alert("Invalid username or password")});
            }
        }
    }

});