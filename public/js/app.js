var config = {
    apiUrl: 'http://localhost:8080'
};

var login = Vue.component('login', {

    template: document.getElementById('login').innerHTML,

    data: function () { return {
        username: '',
        password: ''
    }; },

    methods: {
        _login: function () {
            this.$http.post(
                config.apiUrl + '/login',
                "username=" + encodeURIComponent(this.username) + "&password=" + encodeURIComponent(this.password),
                { headers: { 'Content-Type': 'application/x-www-form-urlencoded' }}
            ).then(function (res) {
                if (res.status === 200) {
                    this.$emit('logged-in');
                } else {
                    alert("Invalid login");
                }
            }, function (res) {
                console.log(0);
                console.log(res);
            });
        },
    }

});


var home = Vue.component('home', {

    template: '<h1>{{ test }}</h1>',

    data: function () { return {
        test: '123'
    }; }

});

var router = new VueRouter({

    routes: [
        { path: '/', component: login },
        { path: '/home', component: home }
    ]

});

router.beforeEach(function (to, form, next) {
    if (to.path != '/' && !app.loggedIn) {
        return router.push('/');
    }
    next();
});

var app = new Vue({

    el: '#app',

    router: router,

    data: {
        loggedIn: false,
    },

    methods: {

        __loggedIn: function () { this.loggedIn = true; router.push('/home'); },

        _logOut: function () {
            this.$http.get(config.apiUrl + '/logout').then(
                function (res) { if (res.status === 200) { this.loggedIn = false; router.push('/'); } },
                function (res) { console.log(res); }
            );
        },
    }

});