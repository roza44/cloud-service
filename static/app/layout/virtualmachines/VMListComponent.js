Vue.component("vm_list", {

    data: function() {
        return {
            vm_list : [],
            role : ""
        }
    },

    template: `
    <div class="container-fluid">
      <h2>Virtuelne masine</h2>
      Kliknite na red u tabeli za izmenu podataka o virtualnim masinama
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>Ime</th>
            <th>Kategorija</th>
            <th>Broj jezgara</th>
            <th>RAM</th>
            <th>Broj GPU jezgara</th>
            <th v-if="role==='superadmin'">Organizacija</th>
          </tr>
        </thead>
        <tbody v-for="v in vm_list">
          <tr>
              <td>{{v.ime}}</td>
              <td>{{v.kategorija.ime}}</td>
              <td>{{v.kategorija.brojJezgara}}</td>
              <td>{{v.kategorija.ram}}</td>
              <td>{{v.kategorija.gpuJezgra}}</td>
              <td v-if="role==='superadmin'">{{v.organizacija.ime}}</td>
          </tr>
        </tbody>
      </table>

    </div>
    
    `,

    methods : {

      add : function(vm) {
        this.vm_list.push(vm);
      }
    },

    mounted () {
        this.role = localStorage.getItem('role');
        var self = this;

        axios
        .get("rest/VirualMachines/getAll")
        .then(response => {
            self.vm_list = response.data;
        })
        .catch(function(error) {
            alert("Failed to read virtual machines");
        });

    }

});