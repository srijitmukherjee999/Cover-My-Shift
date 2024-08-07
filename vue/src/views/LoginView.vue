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
           

            const userRole = response.data.user.role;
            if (userRole == 'admin') {
              this.$router.push("/manager");
            } else {
              this.$router.push("/employee");
            }
            //this.$router.push("/employee"); 
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

#main{
  background-image: url( "../assets/AdobeStock_440299419_Preview.jpeg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
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