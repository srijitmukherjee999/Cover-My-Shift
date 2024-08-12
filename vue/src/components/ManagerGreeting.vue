<template>
  <div class="yes">
      <h1>Hello Manager {{ name }}</h1>
    </div>
</template>

<script>

import ShiftService from '../services/ShiftService';

export default {
  data(){

    return{


        name: ''

    }
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

.yes {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    background-color: white;
    z-index: 2;
  }

  h1{
  font-style: italic;
  animation: fadeIn 4s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>