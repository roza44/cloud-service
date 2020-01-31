Vue.component("monthlyBill", {

    data: function() {
        return {
            bill : {}
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

            <div>
                <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Resurs</th>
                        <th>Cena</th>
                    </tr>
                </thead>
                <tbody v-for="ime in Object.keys(bill)">
                    <tr>
                        <td>{{ime}}</td>
                        <td>{{bill[ime]}}</td>
                    </tr>
                </tbody>
                </table>

                <h4>Ukupna cena: {{ukupnaCena}} evra</h4>
            </div>
        </default_layout>
    `,

    methods: {
        getBill: function() {

        }
    },

    mounted() {
        var self = this;
        $('input[name="daterange"]').daterangepicker({
            opens: 'left'
        }, function(start, end, label) {
                var payload = {
                    from: start.format('DD-MM-YYYY'),
                    to: end.format('DD-MM-YYYY')
                };

                axios
                .post("rest/Organizations/monthlyBill", payload)
                .then(response => {
                    self.bill = response.data;
                })
                .catch(function(error){alert(error.response.data);});
        });
    }

});