<template>
  <body>
    <div id="main" style="">
      <span style="display: inline-block; margin-top: 18%;width: 500px;margin-left: 40%;">
    <h1 style="margin-bottom;:22 %;margin-left:5% ;">Cover My Shift</h1>
  </span>
    <div style="margin-left: 42%; border: 1px solid bal;" id="login">
    <form v-on:submit.prevent="login">
      <h1 >Please Sign In</h1>
      <div role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
      <div role="alert" v-if="this.$route.query.registration">
        Thank you for registering, please sign in.
      </div>
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <button type="submit">Sign in</button>
      <p>
      <router-link v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link></p>
    </form>
  </div>
</div>
  </body>
</template>

<script>
import authService from "../services/AuthService";

export default {
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/employee");
          }
        })
        .catch(error => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>

#main{
  background-image: url( "../assets/AdobeStock_440299419_Preview.jpeg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;

}
.form-input-group {
  margin-bottom: 1rem;
}
label {
  margin-right: 0.5rem;
}

h1 {
  display: flex;
  justify-content: center;
  align-content: center;

}

#login {
  display: flex;
  justify-content: center;
  align-content: center;
  width: 100%;
  height: 100%;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px;
  width: 100%;
  max-width: 400px;
}


</style>