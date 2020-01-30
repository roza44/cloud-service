Vue.component("vm_list", {

    data: function() {
        return {
            vm_list : [],
            role : ""
        }
    },

    template: `
    <div>
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
          <tr v-if="v.visible" v-on:click="changeEmit(v)" >
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
        vm.visible = true;
        this.vm_list.push(vm);
      },

      change : function(vm) {
        vm.visible = true;
        for(i=0; i < this.vm_list.length; i++)
          if(this.vm_list[i].ime === vm.ime) {
              this.vm_list.splice(i, 1, vm);
              break;
          }
      },

      delete : function(vm) {
        for(i=0; i < this.vm_list.length; i++)
          if(this.vm_list[i].ime === vm.ime) {
              this.vm_list.splice(i, 1);
              break;
          }
      },

      changeEmit : function(vm) {
        this.$emit("changeVM", vm);
      },

      searchList : function(filter) {

        this.reset();

        //NAME
        if(filter.name !== "")
          this.vm_list.forEach(element => {
            if(!element.ime.toLowerCase().includes(filter.name.toLowerCase()))
              element.visible = false;
          });

        //KAT
        if(filter.cat !== "")
          this.vm_list.forEach(element => {
            if(!element.kategorija.ime.toLowerCase().includes(filter.cat.toLowerCase()))
              element.visible = false;
          });

        //CPU
        if(filter.cpuLow !== null && filter.cpuLow !== "")
          this.vm_list.forEach(element => {
              if(element.kategorija.brojJezgara <= filter.cpuLow)
                element.visible = false;
          });

        if(filter.cpuHigh !== null && filter.cpuHigh !== "")
          this.vm_list.forEach(element => {
              if(element.kategorija.brojJezgara >= filter.cpuHigh)
                element.visible = false;
          });

        //RAM
        if(filter.ramLow !== null && filter.ramLow !== "")
          this.vm_list.forEach(element => {
              if(element.kategorija.ram <= filter.ramLow)
                element.visible = false;
          });

        if(filter.ramHigh !== null && filter.ramHigh !== "")
          this.vm_list.forEach(element => {
              if(element.kategorija.ram >= filter.ramHigh)
                element.visible = false;
          });

        //GPU
        if(filter.gpuLow !== null && filter.gpuLow !== "")
          this.vm_list.forEach(element => {
              if(element.kategorija.gpuJezgra <= filter.gpuLow)
                element.visible = false;
          });

        if(filter.gpuHigh !== null && filter.gpuHigh !== "")
          this.vm_list.forEach(element => {
              if(element.kategorija.gpuJezgra >= filter.gpuHigh)
                element.visible = false;
          });
        
        this.refresh();
      },

      resetList : function() {
        this.reset();
        this.refresh();
      },

      reset : function() {
        this.vm_list.forEach(element => {element.visible = true;});
      },

      refresh : function() {
        this.vm_list.splice(0,1,this.vm_list[0]);
      }
    },

    mounted () {
        this.role = localStorage.getItem('role');
        var self = this;

        axios
        .get("rest/VirualMachines/getAll")
        .then(response => {
            self.vm_list = response.data;
            this.resetList();
        })
        .catch(function(error) {
            alert(error.response.data);
        });

    }

});