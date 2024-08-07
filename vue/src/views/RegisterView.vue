<template>
  <div id="main">
      <div>
        <h1 id="company">Cover My Shift</h1>
      </div>
      <div id="register" class="text-center">
        <form v-on:submit.prevent="register">
          <h1 id="title-create">Create Account</h1>
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
          <!-- <div class="form-input-group">
            <label for="role">Profile</label>
            <input type="text" id="role" v-model="user.role" required />
          </div> -->

          <div class="form-input-group">
          <label>Profile</label>
          <div>
            <label>
              <input type="radio" value="Manager" v-model="user.role" /> Manager
            </label>
            <label>
              <input type="radio" value="Employee" v-model="user.role" /> Employee
            </label>
          </div>
        </div>

          <button type="submit">Create Account</button>
          <p><router-link v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
        </form>
    </div>
  </div>
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
        role: '', 
        name: '',
        active: true
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {

     // this.user.role = this.determineRole(this.user.username); /// this to determine role

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


    // determineRole(username) {   // method to determine role
    //   if(username.role.includes('admin')){
    //     return 'manager';
    //   }
    //   return 'employee';
    // },


    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
};
</script>

<style scoped>
*{
  padding: 0;
  margin: 0;
}
#main{
  background-image: url( "../assets/AdobeStock_440299419_Preview.jpeg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
}
#title-create{
  font-size: 40px;
  margin-bottom: 20px;
}

.form-input-group {
  display: flex;
  flex-direction: column;
  display: block;
  align-items: center;
  margin-bottom: 1rem;
}

label {
  margin-right: 1rem;
}

#company {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: auto;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px;
  width: 40%;
  margin-top: 2%;
  font-size: 800%;
  }

.text-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: auto;
  background-color: white;
  box-shadow: 0 0 10px;
  border-radius: 8px;
  width: 20%;
  margin-top: 150px;
  min-height: 30vh;
}

</style>
