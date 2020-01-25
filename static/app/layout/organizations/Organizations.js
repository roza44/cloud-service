Vue.component("organizations", {
    template: `
        <default_layout>
            <div class="container-fluid">
                <org_list></org_list>

                <button type="button" 
                class="btn btn-outline-primary btn-lg btn-block"
                @click="$router.push('/openOrg/')"
                >Dodaj organizaciju</button>
            </div>
        </default_layout>
    `

});