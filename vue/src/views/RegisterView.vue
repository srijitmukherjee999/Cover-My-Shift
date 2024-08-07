<template>
  
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
          <div class="form-input-group">
            <label for="role">Manager</label>
            <input type="text" id="role" v-model="user.role" required />
          </div>
          <button type="submit">Create Account</button>
          <p><router-link v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
        </form>
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

body, html {
      height: 100%;
      margin: 0;
      display: flex;
      justify-content: center; 
      align-items: center;     
    }

*{
  padding: 0;
  margin: 0;
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
  padding: 20px;
  color: #4a90e2;
  width: 80%; 
  max-width: 1200px;
  margin: 2% auto; 
  font-size: 9rem; 
  box-sizing: border-box;
  background-color: white; 
}

@media (max-width: 1024px) {
  #company {
    width: 85%; 
    font-size: 1.75rem;
    padding: 20px; 
  }
}


@media (max-width: 768px) {
  #company {
    font-size: 1.5rem; 
    padding: 15px; 
  }
}

@media (max-width: 480px) {
  #company {
    font-size: 1rem; 
    padding: 10px; 
  }
}

.text-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: auto;
  background-color: #4a90e2;
  box-shadow: 0 0 10px;
  border-radius: 8px;
  width: 20%;
  margin-top: 150px auto;
  min-height: 30vh;
}

@media (max-width: 768px) {
  .text-center {
    font-size: 1.5rem; 
    padding: 15px; 
  }
}

@media (max-width: 480px) {
  .text-center {
    font-size: 1rem; 
    padding: 10px; 
  }
}

</style>
