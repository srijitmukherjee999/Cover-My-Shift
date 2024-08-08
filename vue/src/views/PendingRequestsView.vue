<template>
  <company-header/>
    
    <div class="yes">
      <h1>Hello Manager {{ name }}</h1>
    </div>

    <div>
      <nav class="navigation">
        <ul>
          <li><router-link v-bind:to="{ name: 'manager' }">MY HOME</router-link></li>
          <li><router-link v-bind:to="{ name: 'pendingrequests' }">PENDING REQUESTS</router-link></li>
          <li><router-link v-bind:to="{ name: 'pickupshift' }">FIRE EMPLOYEE</router-link></li>
        </ul>
      </nav>
    </div>
    
   
  </template>
  
  <script>
  import CompanyHeader from '../components/CompanyHeader.vue';
  
  export default {
    components: { CompanyHeader },
      
    
  }
  </script>
  
  <style scoped>
  
  
  
  </style>