Vue.component("monthlyBill", {

    data: function() {
        return {
            bill : null
        }
    },

    computed: {
        ukupnaCena: function () {
            var sum = 0;
            for (var ime in this.bill) {
                if (Object.prototype.hasOwnProperty.call(this.bill, ime)) {
                    sum += this.bill[ime];
                }
            }

            return sum;
        }
    },

    template: `
        <default_layout>
            <div class="container-fluid">
                <h3>Mesečni račun</h3>
                <label for="daterange">Period</label>
                <input type="text" ref="daterange" id="daterange" name="daterange" value="" />
            </div>

            <div v-if="bill != null">
                <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Resurs</th>
                        <th>Cena</th>
                    </tr>
                </thead>
                <tbody v-for="ime in Object.keys(bill)">
                    <tr>
                        <td>
                        <svg v-if="ime.startsWith('VM_')" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-cloud"><path d="M18 10h-1.26A8 8 0 1 0 9 20h9a5 5 0 0 0 0-10z"></path></svg>
                        <svg v-else xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-hard-drive"><line x1="22" y1="12" x2="2" y2="12"></line><path d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"></path><line x1="6" y1="16" x2="6.01" y2="16"></line><line x1="10" y1="16" x2="10.01" y2="16"></line></svg>
                        {{ ime.slice(ime.indexOf('_')+1)}}</td>
                        <td>{{bill[ime]}}</td>
                    </tr>
                </tbody>
                </table>

                <h4>Ukupna cena: {{ukupnaCena}} evra</h4>
            </div>

            <div v-else style="padding-left:15px">
                Odaberite period za obračunavanje
            </div>
        </default_layout>
    `,

    methods: {
        getBill: function(start, end) {
            var payload = {
                from: start.format('DD-MM-YYYY'),
                to: end.format('DD-MM-YYYY')
            };

            axios
            .post("rest/Organizations/monthlyBill", payload)
            .then(response => {
                this.bill = response.data;
            })
            .catch(function(error){alert(error.response.data);});
        }
    },

    mounted() {
        var self = this;
        $('input[name="daterange"]').daterangepicker({
            opens: 'left'
        }, function(start, end, label) {
            self.getBill(start, end);
        });
    }

});