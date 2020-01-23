Vue.component("user_list", {

    data: function() {
        return {
            user_list : null
        }
    },

    template: `
    <div class="table-responsive">
    <h2>Korisnici</h2>
    <table class="table table-striped table-sm">
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
        <tr>
            <td></td>
            <td>{{u.email}}</td>
            <td>{{u.lozinka}}</td>
            <td>{{u.ime}}</td>
            <td>{{u.prezime}}</td>
        </tr>
      </tbody>
    </table>
  </div>
    
    `,

    mounted () {

        var self = this;
        
        axios
        .get("rest/Users/getAll")
        .then(response => {
            self.user_list = response.data;
        })
        .error(function(error) {
            alert("Failed to read users");
        });

    }

});