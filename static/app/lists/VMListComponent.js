Vue.component("vm_list", {

    data: function() {
        return {
            vm_list : null
        }
    },

    template: `
    <div class="table-responsive">
    <h2>Virtualne masine</h2>
    <table class="table table-striped table-sm">
      <thead>
        <tr>
          <th></th>
          <th>Ime</th>
          <th>Kategorija</th>
          <th>Broj jezgara</th>
          <th>Broj GPU jezgara</th>
        </tr>
      </thead>
      <tbody v-for="v in vm_list">
        <tr>
            <td></td>
            <td>{{v.ime}}</td>
            <td>{{v.kategorija.ime}}</td>
            <td>{{v.kategorija.ime}}</td>
            <td>{{v.kategorija.brojJezgara}}</td>
            <td>{{v.kategorija.gpuJezgra}}</td>
        </tr>
      </tbody>
    </table>
  </div>
    
    `,

    mounted () {

        var self = this;
        
        axios
        .get("rest/VirualMachines/getAll")
        .then(response => {
            self.vm_list = response.data;
        })
        .error(function(error) {
            alert("Failed to read virtual machines");
        });

    }

});