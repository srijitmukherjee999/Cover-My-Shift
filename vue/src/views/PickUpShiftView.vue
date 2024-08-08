<template>
  <company-header/>
  <employee-greeting/>
  <employee-navigation/>
  
  <div class="emergencyButton">
    <button v-if="showButton" @click="getShiftsByEmergency">EMERGENCY SHIFTS</button>
    <button v-else @click="toggleButton" >Clear</button>
    <div v-for="emergency in emergencyShifts" v-bind:key="emergency.shiftId">
      <p>{{ emergency.assignedName}}</p>
      <p>{{ emergency.startDateTime }}</p>
      <p>{{ emergency.duration }}</p>
        <p>{{ convertStatus(emergency.status) }}</p>
      <p>{{ emergency.emergency }}</p>
    </div>


  </div>

  <div id="data" v-for="shift in listOfShiftsByStatus" v-bind:key="shift">
    <router-link :to="{ name: 'shiftdetails', params: { id: shift.shiftId }} ">
    <div class="bubble" :class="{emergency : shift.emergency && shift.status == 3}">
    <p class="bubble-title">Name</p>
    <p>{{ shift.assignedName }}</p>

    <p class="bubble-title">Start Time</p> 
    <p>{{ shift.startDateTime}}</p>

    <p class="bubble-title">Duration</p> 
    <p>{{ shift.duration }} hours</p>

    <p class="bubble-title">Status</p>
    <p>{{ convertStatus(shift.status) }}</p>

    <p class="bubble-title">Emergency</p> 
    <p>{{ shift.emergency }}</p>
   </div>
  </router-link>


   

  </div>
  
</template>


<script>
import CompanyHeader from '../components/CompanyHeader.vue';
import EmployeeGreeting from '../components/EmployeeGreeting.vue';
import EmployeeNavigation from '../components/EmployeeNavigation.vue';
import ShiftService from '../services/ShiftService';
export default {
  components: { CompanyHeader, EmployeeGreeting, EmployeeNavigation },


  data(){
    return{
      showButton: true,
      listOfShiftsByStatus: [ {

                    assignedName : '',
                    shiftId: 0,
                    assigned: 0,
                    startDateTime: '',
                    duration: 0,
                    status: 0,
                    emergency: false,
                    coverer: 0,
                    covererName: '',
                    description: ''
      }],
      name:'',
      emergencyShifts: [ {
                    assignedName : '',
                    shiftId: 0,
                    assigned: 0,
                    startDateTime: '',
                    duration: '',
                    status: 0,
                    emergency: '',
                    coverer: 0,
                    covererName: '',
                    description: ''
      }]
    }
    
  },
  methods: {

    getShifts(status){
      
      ShiftService.getShiftByStatus(status).then(response => {
         this.listOfShiftsByStatus = response.data;
         
      })

    },
    convertStatus(status){
            
            if(status == 1)
                return "assigned"
            if(status == 2)
            return "uncovered request"
                if(status === 3)
                return "uncovered"
            if(status == 4)
            return "covered"

        },
        getFullName(){

    ShiftService.getUserFullName().then( response => {

       this.name = response.data;

      this.$store.commit("ADD_NAME", this.name);
})

},
  getShiftsByEmergency(){
    
    ShiftService.getEmergencyShifts(true,3).then(response => {
        console.log(response.data);
      this.emergencyShifts = response.data;
      this.showButton = false;
      
      
    })
  },

 /** <button @click="deleteShift(shift.shiftId)">Delete Shift</button>*/
   deleteShift(shiftId) {
    ShiftService.deleteUserShift(shiftId)
        .then(response => {
            if (response.status === 200) {
                this.listOfShiftsByStatus = this.listOfShiftsByStatus.filter(shift => shift.shiftId !== shiftId);
            } else {
                console.error('Unexpected response status:', response.status);
            }
        })
        .catch(error => {
            console.error('Error deleting this shift:', error);
        });
      },
      toggleButton(){
        this.showButton = !this.showButton;
        this.emergencyShifts = [];
      }
},
      
        

  created(){
    this.getShifts(3);
    this.getFullName();
  },
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

#data {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;  
  padding: 20px;
}

.bubble {
  background-color: #4a90e2; 
  color: white;
  border-radius: 50px; 
  padding: 20px; 
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); 
  width: 100%; 
  display: flex;
  flex-wrap: wrap; 
  align-items: center;
  text-align: left;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
}

.bubble:hover {
  transform: scale(1.05); 
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.bubble-title {
  font-weight: bold; 
  margin-bottom: 10px; 
}

.bubble p {
    margin: 0;
    padding: 0;
    margin-right: 20px;
}



.emergency{
    background-color: red;
    text-decoration: underline;
}

.emergencyButton{
  display:flex;
  justify-content: center;
}


</style>