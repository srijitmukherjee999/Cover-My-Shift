<template>
  <section>
    <div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: {{hours}}</h1>
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
    hours: 0,
  }
},
methods: {
 
getFullName(){
  ShiftService.getUserFullName().then( response => {
          this.name = response.data;
          this.$store.commit("ADD_NAME", this.name);
  })
},
getHoursWorked(userId, date){
  ShiftService.getHoursWorkedByUserId(userId, date).then(response => {
    this.hours = response.data;
  })
},
getFirstDayOfWeek() {
      const curr = new Date();
      const first = curr.getDate() - curr.getDay();
      const firstDay = new Date(curr.setDate(first));
      this.date = firstDay.toISOString().split('T')[0]; // Format date as yyyy-mm-dd
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