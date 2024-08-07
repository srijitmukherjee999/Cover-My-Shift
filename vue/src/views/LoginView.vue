<template>
    <div id="main">
      <div>
         <h1 id="company">Cover My Shift</h1>
      </div>
      
      <div id="login" class="text-center">
        <form v-on:submit.prevent="login">
          <h1 >Please Sign In</h1>
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
            <p><router-link v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link></p>
        </form>
      </div>
    </div>
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



</style>