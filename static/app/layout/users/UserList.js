Vue.component("user_list", {

    data: function() {
        return {
            user_list : []
        }
    },

    template: `
    <div>
      <h2>Korisnici</h2>
      Kliknite na red u tabeli za izmenu podataka o korisniku
      <table id="userTable" class="table table-striped table-hover">
        <thead>
          <tr>
            <th></th>
            <th>Korisnicko ime</th>
            <th>Ime</th>
            <th>Prezime</th>
          </tr>
        </thead>
        <tbody v-for="u in user_list">
          <tr id="u.email" v-on:click="showChange(u)">
              <td></td>
              <td>{{u.email}}</td>
              <td>{{u.ime}}</td>
              <td>{{u.prezime}}</td>
          </tr>
        </tbody>
      </table>
      <div class="row">
        <button type="button" class="btn btn-outline-primary btn-lg btn-block" v-on:click="showAdd">
        Dodaj korisnika
        </button>
      </div>
      <user_modal @changeUser="changeUser($event)" @addUser="addUser($event)" ref="modalRef" ></user_modal>
    </div>
    
    `,

    methods: {
      showAdd : function() {
        this.$refs.modalRef.show(null);
      },

      showChange : function(model) {
        this.$refs.modalRef.show(model);
      },
      
      addUser : function(user) {
        this.user_list.push(user);
      },
      
      changeUser : function(user) {
        for(i=0; i < this.user_list.length; i++)
          if(this.user_list[i].email === user.email)
              this.user_list[i] = user;
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