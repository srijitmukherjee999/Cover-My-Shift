<template>
  <section>
    <div id="backImage">
    <div class="overlay"></div>
    <div class="content">
    <div id="main">
      <div>
         <h1 id="company">Cover My Shift</h1>
      </div>
      
      <div id="login" class="text-center">
        <form v-on:submit.prevent="login">
          <h1 >Sign In</h1>
            <div role="alert" v-if="invalidCredentials">
              <p>Invalid username and password!</p>
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
            <p><router-link class="message" v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link></p>
        </form>
      </div>
    </div>
    </div>
    </div>
  </section>
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
            
            const userRole = response.data.user.authorities[0].name;
            if (userRole == 'ROLE_MANAGER') {
              this.$router.push("/manager");
            }else if (userRole == 'ROLE_EMPLOYEE'){
              this.$router.push("/employee");
            }
            //this.$router.push("/employee"); 
          }

        })
        .catch(error => {
          const response = error.response;

          if(response){
            console.log(error.response.status)
            if (response.status === 401) {
              this.invalidCredentials = true;
            }
          } else if (error.request) {
            console.log(error.request)
          } else {
            console.log(error)
          }

          
        });
    }
  }
};
</script>

<style scoped>

section {
  background: transparent;
}

*{
  padding: 0;
  margin: 0;
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
  text-shadow: 10px;
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
  background-color: #4a91e2bc;
  box-shadow: 0 0 10px;
  border-radius: 8px;
  width: 20%;
  margin-top: 150px;
  min-height: 30vh;
  
}


.text-center h1 {
  font-size: 4rem;
  padding: 20px; 
}

[role='alert'] {
  font-size: 1.5rem;
  margin: 10px; 
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
    50% { text-shadow: 0 0 20px silver, 0 0 30px silver; }
    100% { text-shadow: 0 0 5px silver; }
}

.message {
  color: black;
}


</style>