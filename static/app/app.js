const login = {template : '<login></login>'}
const dashboard = {template : '<dashboard></dashboard>'}

const router = new VueRouter({
    mode: 'hash',
    routes: [
      { path: '/', component: login},
      { path: '/dashboard', component: dashboard}
    ]
});

var app = new Vue({
    router,
    el: '#cloudService'
});