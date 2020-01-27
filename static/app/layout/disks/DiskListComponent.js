Vue.component("disk_list", {

    data: function() {
        return {
            disks : []
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
          </tr>
        </thead>
        <tbody v-for="d in disks">
          <tr id="d.ime" @click="showChange(d)">
              <td></td>
              <td>{{d.ime}}</td>
              <td>{{d.tip}}</td>
              <td>{{d.kapacitet}}</td>
          </tr>
        </tbody>
      </table>
      
      <button type="button" class="btn btn-outline-primary btn-lg btn-block" @click="showAdd">
      Dodaj disk
      </button>

      <disk_modal ref="diskRef" @change="changeDisk($event)" @add="addDisk($event)" @deleteDisk="deleteDisk($event)">
      </disk_modal>
    </div>
    
    `,

    methods: {
      showAdd : function() {
        this.$refs.diskRef.show(null);
      },

      showChange : function(model) {
        this.$refs.diskRef.show(model);
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
      .get("rest/Disks/")
      .then(response => {
          self.disks = response.data;
      })
      .catch(function(error) {
          alert("Failed to read disks");
      });
    }

});