<template>
    <!-- <div class="hello"> -->
     <body>
        
   
    <div class="yes">
         <h1>Hello User</h1>
        <h1>Hours Worked: 40</h1>
 
  
    </div>

  <nav>
    <router-link v-bind:to="{name: 'employee'}">MY HOME</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link>
  </nav>
  
  <div class="yes"></div>
  
 








    




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
    flex-grow: 100%;
}

nav {
    text-align: center;
  
}

body{

    background-image: url( "../assets/AdobeStock_440299419_Preview.jpeg");
    background-size: cover;
    height: 100%;
}

</style>