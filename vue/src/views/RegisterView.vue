<template>
   <section>
    <div id="backImage">
    <div class="overlay"></div>
    <div class="content">
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
              <input type="radio" value="ROLE_MANAGER" v-model="user.role" /> Manager
            </label>
            <label>
              <input type="radio" value="ROLE_EMPLOYEE" v-model="user.role" /> Employee
            </label>
          </div>
        </div>

          <button type="submit">Create Account</button>
          <p><router-link class="message" v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
        </form>
    </div>
    </div>
    </div>
    </div>
    </section>
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
  font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
  animation: glow 2.5s infinite alternate;  
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
  background-color: #4a91e2b4;
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

#backImage {
  position: relative;
  height: 100vh;
  background-image: url("..\assets\ben-o-bro-wpU4veNGnHg-unsplash.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}


.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8); 
  z-index: 0; 
}

.content {
  position: relative;
  z-index: 1; 
  padding: 20px;
}

@keyframes glow {
    0% { text-shadow: 0 0 5px silver; }
    50% { text-shadow: 0 0 20px silver; }
    100% { text-shadow: 0 0 5px silver; }
}

.message {
  color: black;
}

</style>
