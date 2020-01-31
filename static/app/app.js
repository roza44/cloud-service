const login = {template : '<login></login>'}
const dashboard = {template : '<dashboard></dashboard>'}
const users = {template : '<users></users>'}
const organizations = {template : '<organizations></organizations>'}
const categories = {template: '<categories></categories>'}
const profile = {template: '<profile></profile>'}
const disks = {template: '<disks></disks>'}
const monthlyBill = {template: '<monthlyBill></monthlyBill>'}

const router = new VueRouter({
    mode: 'hash',
    routes: [
      { path: '/', component: login, meta: {allow : ['user', 'admin', 'superadmin']}},
      { path: '/dashboard', component: dashboard, meta: {allow : ['user', 'admin', 'superadmin']}},
      { path: '/users', component: users, meta: {allow : ['admin', 'superadmin']}},
      { path: '/categories', component: categories, meta: {allow : ['superadmin']}},
      { path: '/organizations', component: organizations, meta: {allow : ['admin', 'superadmin']}},
      { path: '/profile', component: profile, meta: {allow : ['user', 'admin', 'superadmin']}},
      { path: '/disks', component: disks, meta: {allow : ['user', 'admin', 'superadmin']}},
      { path: '/monthlyBill', component: monthlyBill, meta: {allow : ['admin']}}
    ]
});

//Izvrsava se pre svake izmene rute
router.beforeEach((to, from, next) => {
  // to and from are both route objects. must call `next`.
  var self = this;
  var ei;
  axios
  .get("rest/userInfo")
  .then(response => {
    
    ei = response.data;
    if(!ei.isLogedIn) {
      if(to.path === "/")
        next();
      else
        next("/");
    }
    else if(to.path === "/")
      next(from.path);
    else if(to.meta.allow.includes(ei.role))
    {
      localStorage.setItem("role", ei.role);
      localStorage.setItem("username", ei.username);
      localStorage.setItem("orgName", ei.orgName);
      next();
    }
    else 
      next(from.path);
  })
  .catch(function(error){alert(error.response.data);});

})

var app = new Vue({
    router,
    el: '#cloudService'
});