Vue.component("user_list", {

    data: function() {
        return {
            user_list : []
        }
    },

    template: `
    <div class="container-fluid">
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
      <button type="button" class="btn btn-outline-primary btn-lg btn-block" v-on:click="showAdd">
      Dodaj korisnika
      </button>
      <user_modal ref="modalRef" @changeUser="changeUser($event)" @addUser="addUser($event)" @deleteUser="deleteUser($event)">
      </user_modal>
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
          if(this.user_list[i].email === user.email) {
              this.user_list.splice(i, 1, user);
              break;
          }
      },
      
      deleteUser : function(user) {
        for(i=0; i < this.user_list.length; i++)
          if(this.user_list[i].email === user.email) {
              this.user_list.splice(i, 1);
              break;
          }
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