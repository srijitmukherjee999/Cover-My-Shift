<template>
  <section>
    <div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: {{getHoursWorked(this.$store.state.currentUser.id, getFirstDayOfWeek)}}</h1>
 </div>
  </section>
</template>

<script>

import ShiftService from '../services/ShiftService';

export default {
data(){
  return{
    name:'',
  }
},
methods: {
 
getFullName(){
  ShiftService.getUserFullName().then( response => {
          this.name = response.data;
          this.$store.commit("ADD_NAME", this.name);
  })
},
getHoursWorked(userId, week){
  ShiftService.getUserByUserId(userId, week).then(response => {
    this.hours = response.data;
  })
},
getFirstDayOfWeek(){
  var curr = new Date; // get current date
  return curr.getDate() - curr.getDay(); // First day is the day of the month - the day of the week
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

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

</style>