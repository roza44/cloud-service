Vue.component("profile", {

    data : function() {
        return {
            user : ""
        }

    },

    template : `
    <default_layout>
        <div class="container-fluid" style="position:absolute;left:0;top:0;right:0;bottom:0;padding-top:100px;">
            <div class="text-center">
                <img src="../data/avatar_2x.png" class="rounded-circle">
                <h1>{{user.email}}</h1>
                <hr/>
                <h4 class="text-muted">Ime: <p class="lead">{{user.ime}}</p></h4>
                <h4 class="text-muted">Prezime: <p class="lead">{{user.prezime}}</p></h4>
                <h4 class="text-muted">Lozinka: <p class="lead">{{user.lozinka}}</p></h4>
                <hr/>
                <button v-on:click="showModal" type="button" class="btn btn-info">Izmeni profil</button>
            </div>
            <profile_modal ref="pModal" @changeProfile="update($event)" ></profile_modal>    
        </div>      
    </default_layout>
    `,

    methods : {
        showModal : function() {
            this.$refs.pModal.show(this.user);
        },

        update : function(model) {
            this.user = model;
        }
    },

    mounted () {
        var username = localStorage.getItem('username');
        var self = this;

        axios
        .get("rest/Users/getUser/" + username)
        .then(response => { 
            self.user = response.data;
        });
    }

});