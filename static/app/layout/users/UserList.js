Vue.component("user_list", {

    data: function() {
        return {
            user_list : null
        }
    },

    template: `
    <div>
    <h2>Korisnici</h2>
    Kliknite na red u tabeli za izmenu podataka o korisniku
    <table class="table table-striped table-hover">
      <thead>
        <tr>
          <th></th>
          <th>Korisnicko ime</th>
          <th>Lozinka</th>
          <th>Ime</th>
          <th>Prezime</th>
        </tr>
      </thead>
      <tbody v-for="u in user_list">
        <tr v-on:click="changeUser(u)">
            <td></td>
            <td>{{u.email}}</td>
            <td>{{u.lozinka}}</td>
            <td>{{u.ime}}</td>
            <td>{{u.prezime}}</td>
        </tr>
      </tbody>
    </table>
    <div class="row">
      <button type="button" class="btn btn-outline-primary btn-lg btn-block" v-on:click="addUser">
      Dodaj korisnika
      </button>
    </div>
    <user_modal ref="modalRef"></user_modal>
  </div>
    
    `,

    methods: {
      addUser : function() {
        this.$refs.modalRef.show(null);
      },

      changeUser : function(model) {
        this.$refs.modalRef.show(model);
      }
    },

    mounted () {

        var self = this;
        
        axios
        .get("rest/Users/getAll")
        .then(response => {
            self.user_list = response.data;
        })
        .catch(function(error) {
            alert("Failed to read users");
        });

    }

});