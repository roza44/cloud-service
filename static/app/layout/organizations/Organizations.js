Vue.component("organizations", {
    template: `
        <default_layout>
            <div class="container">
                <div class="row">
                    <org_list></org_list>
                </div>

                <div class="row">
                    <button type="button" 
                        class="btn btn-outline-primary btn-lg btn-block"
                        @click="$router.push('/openOrg/')"
                        >Dodaj organizaciju</button>
                </div>
            </div>
        </default_layout>
    `

});