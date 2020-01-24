const login = {template : '<login></login>'}
const dashboard = {template : '<dashboard></dashboard>'}
const users = {template : '<users></users>'}
const organizations = {template : '<organizations></organizations>'}
const openOrganization = {template: '<open_organization></open_organization>'}

const router = new VueRouter({
    mode: 'hash',
    routes: [
      { path: '/', component: login},
      { path: '/dashboard', component: dashboard},
      { path: '/users', component: users },
      { path: '/organizations', component: organizations},
      { path: '/openOrg/:ime', component: openOrganization},
      { path: '/openOrg/', component: openOrganization}
    ]
});

//Izvrsava se pre svake izmene rute
router.beforeEach((to, from, next) => {
  // to and from are both route objects. must call `next`.
  var self = this;
  var ei;
  axios
  .get("rest/Users/info")
  .then(response => {
    
    ei = response.data;
    if(!ei.isLogedIn && to.path !== "/") {
      next("/");
    }
    else
    {
      localStorage.setItem("role", ei.role)
      next();
    }
  })
  .catch(function(error){alert(error);});

})

var app = new Vue({
    router,
    el: '#cloudService'
});