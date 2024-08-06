<template>
  <h1>Hello {{ name }}</h1>
  <nav>
    <router-link v-bind:to="{name: 'employee'}">MY HOME</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link>
  </nav>
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

di
RequestOffFormv {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
}

nav {
    text-align: center;
    font-size: larger;
}

</style>