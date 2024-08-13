<template>
  
    <div id="company-name">
    <h1>COVER MY SHIFT</h1>
    <button><router-link class="logout" v-bind:to="{ name: 'logout' }" v-if="this.$store.state.token != ''">Logout</router-link></button>
  </div>
  
</template>

<script>
export default {

}
</script>

<style scoped>

.logout{
    font: normal;
    text-decoration: none;

}

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
    text-decoration: none;
}

button:hover {
    background: #d83a56;
    border-color: #ff6f61;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
    text-decoration: none;
}

button:active {
    background: #ff6f61;
    border-color: #d83a56;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    transform: translateY(0); /* Return to original position */
    text-decoration: none;
}

button:focus {
    outline: none;
    text-decoration: none;
}

#company-name {
  padding: 5px;
  color: #4a90e2;
  display: flex;
  justify-content: space-between;
}

h1 {
    font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
}

</style>