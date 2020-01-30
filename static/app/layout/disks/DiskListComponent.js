Vue.component("disk_list", {

    data: function() {
        return {
            disks : [],
            role : null
        }
    },

    template: `
    <div class="container-fluid">
      <div class="row">
        <div class="col-8">
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
              <tr v-if="d.visible" id="d.ime" @click="showModal(d)">
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
        </div>
        <div class="col-4">
            <disc_search @search="search($event)"></disc_search>
        </div>
      </div>
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
        disk.visible = true;
        this.disks.push(disk);
      },
      
      changeDisk : function(disk) {
        disk.visible = true;
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
      },

      search : function(filter) {

        this.reset();

        if(filter.name !== "")
          this.disks.forEach(element => {
              if(!element.ime.toLowerCase().includes(filter.name.toLowerCase()))
                element.visible = false;
          });

        if(filter.cpuLow !== null && filter.cpuLow !== "")
        this.disks.forEach(element => {
            if(element.kapacitet <= filter.cpuLow)
              element.visible = false;
        });

        if(filter.cpuHigh !== null && filter.cpuHigh !== "")
        this.disks.forEach(element => {
            if(element.kapacitet >= filter.cpuHigh)
              element.visible = false;
        });

        if(filter.type !== "")
          this.disks.forEach(element => {
              if(!element.tip.toLowerCase().includes(filter.type.toLowerCase()))
                element.visible = false;
          });

        this.refreshVue();
        
      },

      reset : function() {
        this.disks.forEach(element => {element.visible = true});
      },

      refreshVue : function() {
        this.disks.splice(0,1,this.disks[0]);
      },

      getDiscs : function() {
        var self = this;
        
        axios
        .get("rest/Disks/") // Only returns disks this account can see
        .then(response => {
            self.disks = response.data;
            this.reset();
            this.refreshVue();
        })
        .catch(function(error) {
            alert("Failed to read disks");
        });
      }
    },

    mounted () {
      
      this.getDiscs();
      this.role = localStorage.getItem('role');
    }

});