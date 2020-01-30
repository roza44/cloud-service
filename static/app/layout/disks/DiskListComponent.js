Vue.component("disk_list", {

    data: function() {
        return {
            disks : [],
            role : null
        }
    },

    template: `
    <div class="container-fluid">
      <h2>Diskovi</h2>
      Kliknite na red u tabeli da izmenite parametre diska
      <table id="diskTable" class="table table-striped table-hover">
        <thead>
          <tr>
            <th></th>
            <th>Ime</th>
            <th>Tip</th>
            <th>Kapacitet</th>
            <th>VM</th>
            <th v-if="role === 'superadmin'">Organizacija</th>
          </tr>
        </thead>
        <tbody v-for="d in disks">
          <tr id="d.ime" @click="showModal(d)">
              <td></td>
              <td>{{d.ime}}</td>
              <td>{{d.tip}}</td>
              <td>{{d.kapacitet}}</td>
              <td v-if="d.vm">{{d.vm.ime}}</td>
              <td v-else>/</td>
              <td v-if="role === 'superadmin'">{{d.organizacija.ime}}</td>
          </tr>
        </tbody>
      </table>
      
      <button v-if="role !== 'user'" type="button" class="btn btn-outline-primary btn-lg btn-block" @click="showModal(null)">
      Dodaj disk
      </button>

      <disk_modal ref="diskRef" @change="changeDisk($event)" @add="addDisk($event)" @deleteDisk="deleteDisk($event)">
      </disk_modal>
    </div>
    
    `,

    methods: {
      showModal : function(model) {
        axios
        .get("rest/Organizations") // Will return only those the current account can see
        .then(response => {
          this.$refs.diskRef.show(model, response.data);
        })
        .catch(function(error) {alert(error.response.data);});
      },
      
      addDisk : function(disk) {
        this.disks.push(disk);
      },
      
      changeDisk : function(disk) {
        for(i=0; i < this.disks.length; i++)
          if(this.disks[i].ime === disk.ime) {
              this.disks.splice(i, 1, disk);
              break;
          }
      },
      
      deleteDisk : function(ime) {
        for(i=0; i < this.disks.length; i++)
          if(this.disks[i].ime === ime) {
              this.disks.splice(i, 1);
              break;
          }
      }
    },

    mounted () {
      var self = this;
        
      axios
      .get("rest/Disks/") // Only returns disks this account can see
      .then(response => {
          self.disks = response.data;
      })
      .catch(function(error) {
          alert(error.response.data);
      });

      this.role = localStorage.getItem('role');
    }

});