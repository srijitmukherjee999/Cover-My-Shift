<template>
  <body>
    <company-header/>
    <employee-greeting/>
    <div id="backImage">
    <div class="overlay"></div>
    <div class="content">
    <employee-navigation/>
  
  
    <div id="emergency-button">
      <div>
    <button class="emergencyButton" v-if="showButton" @click="getShiftsByEmergency">EMERGENCY SHIFTS</button>
    <button class="clearButton" v-else @click="toggleButton" >Clear</button>
  </div>
    
    <div id="emergency-button">
    <div class="together">
    <div class="emergencybubble" v-for="emergency in emergencyShifts" v-bind:key="emergency.shiftId">
      <div id="shiftObjects"><p class="bubble-title">Name: {{ emergency.assignedName}}</p></div>
      <div id="shiftObjects"><p class="bubble-title">Start Time: {{ emergency.startDateTime }}</p></div>
      <div id="shiftObjects"><p class="bubble-title">Duration: {{ emergency.duration }}</p></div>
      <div id="shiftObjects"><p class="bubble-title">{{ convertStatus(emergency.status) }}: {{ emergency.emergency }}</p></div>
    </div>
  </div>
  </div>

  <div id="data" v-if="showButton" v-for="shift in listOfShiftsByStatus" v-bind:key="shift">
    <router-link :to="{ name: 'shiftdetails', params: { id: shift.shiftId }} ">
      <div class="bubble" :class="{emergency : shift.emergency && shift.status == 3, green: shift.status == 4 || shift.status == 1}" >
        <div id="shiftObjects"><p class="bubble-title">Name: {{ shift.assignedName }}</p></div>
      
      
        <div id="shiftObjects"><p class="bubble-title">Start Time: {{ shift.startDateTime }}</p></div>
      
      
        <div id="shiftObjects"><p class="bubble-title">Duration: {{ shift.duration }} <span>hours</span></p></div>
      
      
        <div id="shiftObjects"><p class="bubble-title">Status: {{ convertStatus(shift.status) }}</p></div>
      
      
        <div id="shiftObjects"><p class="bubble-title">Emergency: {{ shift.emergency }}</p></div>
      </div>
    </router-link>
  </div> 
  </div>
  </div>
  </div>
</body>
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
      emergencyShifts: []
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

body {
  background: transparent;
}

#data {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;  
  padding: 20px;
}
.together {
      display: flex;
      justify-content: center; 
      width: 100%; 
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

.container {
  display: flex;
  flex-direction: row; 
  flex-wrap: wrap; 
}

@media (max-width: 600px) {
  .container {
    flex-direction: column; 
  }
}

.bubble-title {
  font-weight: bold; 
  flex: 1 1 auto; 
  padding: 15px;
  margin: 10px;
  border-radius: 5px;
  text-align: center;
  font-weight: bold; 
}

.bubble p {
    margin: 0;
    padding: 0;
    margin-right: 20px;
}



.emergency{
    background-color: red;
    text-decoration: underline;
    animation : vertical-shaking 4s infinite;
}

.emergencyButton{
  display:flex;
  justify-content: center;
}

h1{
  font-style: italic;
  font-weight: bold;
  animation: fadeIn 2s;
}

@keyframes vertical-shaking {
  0% { transform: translateY(0) }
  25% { transform: translateY(5px) }
  50% { transform: translateY(-5px) }
  75% { transform: translateY(5px) }
  100% { transform: translateY(0) }
}

#backImage {
  position: relative;
  height: 100vh;
  background-image: url("..\assets\nastuh-abootalebi-eHD8Y1Znfpk-unsplash.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}


.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8); 
  z-index: 0; 
}

.content {
  position: relative;
  z-index: 1; 
  padding: 20px;
}

#emergency-button {
    padding: 20px;
    margin: 20px;
    text-align: center;
    border-radius: 5px;
    border-color: red;
}

.emergencyButton {
    display: inline;
    margin: auto;
    font-size: larger;
    color: red;
    font-weight: bold;
    border-radius: 50px;
    padding: 20px;
    box-shadow: 0 4px 8px;
    max-width: 100%; 
    transition: transform 0.3s, box-shadow 0.3s;
    font: bold;
    border-color: red;
    border-width: 20px;
}

.clearButton {
    display: inline;
    margin: auto;
    font-size: larger;
    color: red;
    font-weight: bold;
    border-radius: 50px;
    padding: 20px;
    box-shadow: 0 4px 8px;
    max-width: 100%; 
    transition: transform 0.3s, box-shadow 0.3s;
    font: bold;
    border-color: red;
    border-width: 20px;
}

.emergencybubble {
  background-color: red; 
  color: white;
  border-radius: 50px; 
  padding: 20px; 
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); 
  max-width: 100%;
  width: auto; 
  display: flex;
  flex-direction: row; 
  align-items: flex-start;
  text-align: left;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
}

.emergencybubble:hover {
  transform: scale(1.05); 
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.container {
  display: flex;
  flex-direction: row; 
  flex-wrap: wrap; 
}

@media (max-width: 600px) {
  .container {
    flex-direction: column; 
  }
}

#emergency-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;  
  padding: 20px;
}
</style>