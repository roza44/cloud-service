Vue.component("cat_list", {

    data: function() {
        return {
            cat_list : []
        }
    },

    template: `
    <div class="container-fluid">
      <h2>Kategorije</h2>
      Kliknite na red u tabeli za izmenu podataka o kategoriji
      <table id="catTable" class="table table-striped table-hover">
        <thead>
          <tr>
            <th></th>
            <th>Ime</th>
            <th>Broj Jezgara</th>
            <th>RAM</th>
            <th>Broj GPU jezgara</th>
          </tr>
        </thead>
        <tbody v-for="c in cat_list">
          <tr id="c.ime" v-on:click="showChange(c)">
              <td></td>
              <td>{{c.ime}}</td>
              <td>{{c.brojJezgara}}</td>
              <td>{{c.ram}}</td>
              <td>{{c.gpuJezgra}}</td>
          </tr>
        </tbody>
      </table>
      <button type="button" class="btn btn-outline-primary btn-lg btn-block" v-on:click="showAdd">
      Dodaj kategoriju
      </button>
      <cat_modal ref="catRef" @changeCat="changeCat($event)" @addCat="addCat($event)" @deleteCat="deleteCat($event)">
      </cat_modal>
    </div>
    
    `,

    methods: {
      showAdd : function() {
        this.$refs.catRef.show(null);
      },

      showChange : function(model) {
        this.$refs.catRef.show(model);
      },
      
      addCat : function(cat) {
        this.cat_list.push(cat);
      },
      
      changeCat : function(cat) {
        for(i=0; i < this.cat_list.length; i++)
          if(this.cat_list[i].ime === cat.ime) {
              this.cat_list.splice(i, 1, cat);
              break;
          }
      },
      
      deleteCat : function(cat) {
        for(i=0; i < this.cat_list.length; i++)
          if(this.cat_list[i].ime === cat.ime) {
              this.cat_list.splice(i, 1);
              break;
          }
      }
    },

    mounted () {
      var self = this;
        
      axios
      .get("rest/Categories/getAll")
      .then(response => {
          self.cat_list = response.data;
      })
      .catch(function(error) {
          alert("Failed to read categories");
      });
    }

});