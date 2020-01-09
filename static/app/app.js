const login = {template : '<login></login>'}

const router = new VueRouter({
    mode: 'hash',
    routes: [
      { path: '/', component: login}
    ]
});

var app = new Vue({
    router,
    el: '#cloudService'
});