Vue.component("default_layout", {

    data: function() {
        return {
            role : ""
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
                            <router-link class="nav-link" to="/dashboard">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-home"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path><polyline points="9 22 9 12 15 12 15 22"></polyline></svg>
                                Virtuelne mašine <span class="sr-only">(current)</span>
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" to="/organizations">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-layers"><polygon points="12 2 2 7 12 12 22 7 12 2"></polygon><polyline points="2 17 12 22 22 17"></polyline><polyline points="2 12 12 17 22 12"></polyline></svg>
                                Organizacije
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" to="/users">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-users"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                Korisnici
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" to="/categories">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-tag"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                Kategorije
                            </router-link>
                        </li>
                    </ul>
                </div>
            </nav>

            <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-1" style="position:absolute;left:0;top:0;right:0;bottom:0;padding-top:60px;">
                <slot></slot>
            </main>
        </div>
        </div>
    </div>
    
    `,

    mounted () {
        this.role = localStorage.getItem('role');
    },
 

    methods: {
        logout : function () {
            axios
            .get("rest/Users/logout")
            .then(response  => {
                this.$router.push("/");
            })
            .catch(function(error){alert(error);})
        }

    }

});