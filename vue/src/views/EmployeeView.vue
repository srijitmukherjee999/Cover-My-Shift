<template>
     <body>
        
   
    <div class="yes">
         <h1>Hello User</h1>
        <h1>Hours Worked: 40</h1>
 
  
    
    </div>
  <nav class="navigation">
    <ul>
        <li><router-link v-bind:to="{name: 'employee'}">MY HOME</router-link></li>
    
        <li><router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link></li>
    
        <li><router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link></li>
    </ul>
  </nav>
  
  </body>   
</template>

<script>
import RequestTimeOffView from './RequestTimeOffView.vue';

export default {

}
</script>

<style scoped>

.yes{
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    background-color: white;
}

.navigation {
    /* border: 2px solid white; */
    padding: 10px;
    margin: 20px;
    border-radius: 5px; 
    /* background-color: white; */
}

.navigation ul {
    list-style: none;
    padding: 0;
    margin: center;
    text-align: center;

}

.navigation li {
    display: inline;
    margin-right: 15px;

    font-size: larger;
    
}

body{

    background-image: url( "../assets/AdobeStock_440299419_Preview.jpeg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
}

</style>