<template>
  <section>
    <div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: {{getHoursWorked(user.id, date)}}</h1>
 </div>
  </section>
</template>

<script>

import ShiftService from '../services/ShiftService';

export default {
data(){
  return{
    name:'',
    user: {},
    date: '',
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
  this.date = curr.getDate() - curr.getDay(); 
  // First day is the day of the month - the day of the week
  console.log(this.date)
},

getUserId(){
  ShiftService.getUserFromUsername().then(response => {
    this.user = response.data;
  })
}


},
created(){
  this.getFullName();
  this.getFirstDayOfWeek();
  this.getUserId();
  
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