Vue.component("default_layout", {

    data: function() {
        return {
            role : "",
            orgName : ""
        }
    },

    template: `

    <div class="container-fluid">
        <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
            <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">CSService</a>
            
            <ul class="navbar-nav px-3">
                <li class="nav-item text-nowrap">
                <a v-on:click="logout" class="nav-link">Odjava</a>
                </li>
            </ul>
        </nav>

        <div class="container-fluid">
        <div class="row">
            <nav class="col-md-2 d-none d-md-block bg-light sidebar">
                <div class="sidebar-sticky">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <router-link class="nav-link" to="/profile">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-home"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path><polyline points="9 22 9 12 15 12 15 22"></polyline></svg>
                                Profil <span class="sr-only">(current)</span>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" to="/dashboard">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-cloud"><path d="M18 10h-1.26A8 8 0 1 0 9 20h9a5 5 0 0 0 0-10z"></path></svg>
                                Virtuelne mašine <span class="sr-only">(current)</span>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" to="/disks">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-hard-drive"><line x1="22" y1="12" x2="2" y2="12"></line><path d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"></path><line x1="6" y1="16" x2="6.01" y2="16"></line><line x1="10" y1="16" x2="10.01" y2="16"></line></svg>
                                Diskovi <span class="sr-only">(current)</span>
                            </router-link>
                        </li>
                        <li v-if="role==='superadmin'" class="nav-item">
                            <router-link class="nav-link" to="/organizations">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-layers"><polygon points="12 2 2 7 12 12 22 7 12 2"></polygon><polyline points="2 17 12 22 22 17"></polyline><polyline points="2 12 12 17 22 12"></polyline></svg>
                                Organizacije
                            </router-link>
                        </li>
                        <li v-if="role==='admin'" class="nav-item" @click="openOrg()">
                            <router-link class="nav-link" to="#">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-layers"><polygon points="12 2 2 7 12 12 22 7 12 2"></polygon><polyline points="2 17 12 22 22 17"></polyline><polyline points="2 12 12 17 22 12"></polyline></svg>
                                {{orgName}}
                            </router-link>
                        </li>
                        <li v-if="role==='superadmin' || role==='admin'" class="nav-item">
                            <router-link class="nav-link" to="/users">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-users"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                Korisnici
                            </router-link>
                        </li>
                        <li v-if="role==='superadmin'" class="nav-item">
                            <router-link class="nav-link" to="/categories">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-tag"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path><line x1="7" y1="7" x2="7.01" y2="7"></line></svg>
                                Kategorije
                            </router-link>
                        </li>
                        <li v-if="role==='admin'" class="nav-item">
                            <router-link class="nav-link" to="/monthlyBill">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-dollar-sign"><line x1="12" y1="1" x2="12" y2="23"></line><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
                                Mesečni račun
                            </router-link>
                        </li>
                    </ul>
                </div>
            </nav>

            <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-1" style="position:absolute;left:0;top:0;right:0;bottom:0;padding-top:60px;">
                <slot></slot>
            </main>

            <org_modal ref="orgModal_nav">
            </org_modal>
        </div>
        </div>
    </div>
    
    `,

    mounted () {
        this.role = localStorage.getItem('role');
        this.orgName = localStorage.getItem('orgName');
    },
 

    methods: {
        logout : function () {
            axios
            .get("rest/Users/logout")
            .then(response  => {
                this.$router.push("/");
            })
            .catch(function(error){alert(error.response.data);})
        },

        openOrg : function () {
            axios.get("rest/Organizations/" + this.orgName)
            .then(response => {
                this.$refs.orgModal_nav.show(response.data);
            });
        }

    }

});