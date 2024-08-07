<template>
  <body>
  <div id="capstone-app">
  <div id="company-name">
    <h1>COVER MY SHIFT</h1>
    <button><router-link v-bind:to="{ name: 'logout' }" v-if="this.$store.state.token != ''">Logout</router-link></button>
  </div>
    <!--<router-link v-bind:to="{ name: 'home' }">Home</router-link>&nbsp;|&nbsp;
    </div> -->
    <router-view />
  </div>
</body>
</template>

<script>

export default {
  
    
  
}
</script>

<style scoped>

button {
    padding: 20px; 
    padding: 12px 24px;
    font-size: 16px;
    font-weight: bold;
    color: #fff;
    text-align: center;
    text-decoration: none;
    border-radius: 50px;
    background: #ff6f61;
    border: 2px solid #d83a56;
    transition: all 0.3s ease;
}

button:hover {
    background: #d83a56;
    border-color: #ff6f61;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
}

button:active {
    background: #ff6f61;
    border-color: #d83a56;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    transform: translateY(0); /* Return to original position */
}

button:focus {
    outline: none;
}

#company-name {
  padding: 5px;
  color: #4a90e2;
  display: flex;
  justify-content: space-between;
}

</style>

