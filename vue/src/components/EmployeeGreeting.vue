<template>
  <section>
    <div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: 40</h1>
 </div>
  </section>
</template>

<script>

import ShiftService from '../services/ShiftService';

export default {
data(){
  return{
    name:''
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

.yes{
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    background-color: white;
}


h1{
  font-style: italic;
  animation: fadeIn 4s;
}

</style>