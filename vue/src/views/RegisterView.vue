<template>
  <body>
    <div id="main" style="">
      <span style="display: flex; justify-content: space-evenly; align-content: center;">
    <h1 id="company" style="margin-bottom: 35%; margin-left: 29%;">Cover My Shift</h1>
  </span>
  <div id="register" class="text-center">
    <form v-on:submit.prevent="register">
      <h1>Create Account</h1>
      <div role="alert" v-if="registrationErrors">
        {{ registrationErrorMsg }}
      </div>
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="fullName">Full Name</label>
        <input type="text" id="fullName" v-model="user.name" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <div class="form-input-group">
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" required />
      </div>
      <div class="form-input-group">
        <label for="role">Manager</label>
        <input type="text" id="role" v-model="user.role" required />
      </div>
      <button type="submit">Create Account</button>
      <p><router-link v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
    </form>
  </div>
  </div>
  </body>
</template>

<script>
import authService from '../services/AuthService';

export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
        name: '',
        active: true
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
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

body {
  height: 100%;
  margin: 0;
}
.form-input-group {
  margin-bottom: 1rem;
}
label {
  margin-right: 0.5rem;
}

#company {
  display: flex;
  justify-content: center;
  align-content: center;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px;
  width: 45%;
  margin-top: 3%;
  font-size: 800%;
  }

.text-center {
  display: flex;
  align-content: center;
  justify-content: center;
  background-color: white;
  box-shadow: 0 0 10px;
  border-radius: 8px;
  width: 30%;
  padding: 20px;
  
}



</style>
