<template>
    <div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: 40</h1>
 </div>
  <div>
    <nav class="navigation">
    <ul>
        <li><router-link v-bind:to="{name: 'employee'}">MY HOME</router-link></li>
    
        <li><router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link></li>
    
        <li><router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link></li>

        <li><router-link v-bind:to="{name: 'myshift'}">MY SHIFT</router-link></li>
    </ul>
  </nav>
  </div>
  <request-off-form/>
</template>

<script>
import RequestOffForm from '../components/RequestOffForm.vue'
import ShiftService from '../services/ShiftService';
export default {
  data(){
    return {
      name: ''
    }
    
  },
    components: {
        RequestOffForm
    },
    methods: {
      getFullName(){

  ShiftService.getUserFullName().then( response => {

         this.name = response.data;

        this.$store.commit("ADD_NAME", this.name);
})

},
    },
    created(){
      this.getFullName();
    }

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
    padding: 10px;
    margin: 20px;
    border-radius: 5px; 
}

.navigation a {
  text-decoration: none;
  color: #000000;
}

.navigation ul {
    list-style: none;
    padding: 0;
    margin: 0;
    text-align: center;
}

.navigation li {
    display: inline;
    margin-right: 15px;
    font-size: larger;
    background-color: white;
    color: black;
    border-radius: 50px;
    padding: 20px;
    box-shadow: 0 4px 8px;
    width: 100%; 
    transition: transform 0.3s, box-shadow 0.3s;
    font: bold;
}

.navigation li:hover {
    transform: scale(1.05); 
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
    background-color: lightgray;
}

@media (max-width: 768px) {
    .navigation li {
        font-size: medium; 
        padding: 15px; 
    }
}

@media (max-width: 480px) {
    .navigation li {
        font-size: small; 
        padding: 10px; 
    }

    .navigation ul {
        display: flex;
        flex-direction: column;
        flex-wrap: wrap; 
    }
}

.request-off-form {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
}


h1{
  font-style: italic;
  font-weight: bold;
  animation: fadeIn 2s;
}


</style>