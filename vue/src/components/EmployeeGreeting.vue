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
  async getFullName() {
    const response = await ShiftService.getUserFullName();
    this.name = response.data;
    this.$store.commit("ADD_NAME", this.name);
  },
  async getHoursWorked(userId, date) {
    console.log('Calling getHoursWorked with userId:', userId, 'and date:', date);
    const response = await ShiftService.getHoursWorkedByUserId(userId, date);
    console.log('Hours worked response:', response.data);
    this.hours = response.data;
  },
  getFirstDayOfWeek() {
    const curr = new Date();
    const first = curr.getDate() - curr.getDay();
    const firstDay = new Date(curr.setDate(first));
    this.date = firstDay.toISOString().split('T')[0];
    console.log('First day of the week:', this.date);
  },
  async getUserId() {
    const response = await ShiftService.getUserFromUsername();
    this.user = response.data;
    console.log('User data:', this.user);
  }
},
created: async function() {
  try {
    await this.getFullName();
    this.getFirstDayOfWeek(); 
    await this.getUserId(); 
    await this.getHoursWorked(this.user.id, this.date);
  } catch (error) {
    console.error("An error occurred during the creation process:", error);
  }
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