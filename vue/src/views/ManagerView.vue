<template>
  <section>
    <div class="fixed-header">
      <company-header />
      <manager-greeting />
      <div id="backImage">
        <div class="overlay"></div>
        <div class="content">
          <manager-navigation />

          <div id="shift-inputs">
            <div class="filter">
              <div>
                <input type="date" v-model="shiftInputs.startDate" placeholder="Start Date" :min="dateNow"/>
              </div>
              <div>
                <input type="date" v-model="shiftInputs.endDate" placeholder="End Date (optional)" :min="dateNow"/>
              </div>
              <div>
                <input type="time" v-model="shiftInputs.startTime" placeholder="Start Time" />
              </div>
              <div>
                <input type="number" v-model="shiftInputs.duration" placeholder="Duration (hours)" min="0" max="8"/> 
              </div>


              <div>
                <button class="submit-button" @click="submitShifts">
                  Submit Shifts
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="scrollable-container">
        <div class="scrollable-content">
          <div class="content">
            <div id="data" v-for="user in listOfUsers" :key="user.id">
              <div class="together" >
                <div class="bubble" v-bind:class="{grey: isShiftBetweenVacation(user)}">
                  <div class="bubble-title"  >
                    <p>{{ user.fullName }}</p>
                  </div>
                  <div class="bubble-title">
                    <p>Hours worked week of:</p>
                    <p v-for="h in user.hours" v-bind:key="h">{{ h }}</p>
                  </div>
                  <div>
                    <button  v-if="!isShiftBetweenVacation(user)"  :class="[
                      'add-shift-button',
                      isSelected(user.id) ? 'selected-button' : 'add-button',
                    ]" @click="toggleSelection(user.id)">
                      {{ isSelected(user.id) ? "Selected" : "Add Shift" }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
  
<script>
import ShiftService from "../services/ShiftService.js";
import AuthService from "../services/AuthService";
import CompanyHeader from "../components/CompanyHeader.vue";
import ManagerNavigation from "../components/ManagerNavigation.vue";
import ManagerGreeting from "../components/ManagerGreeting.vue";
import ManagerService from '../services/ManagerService.js';

export default {
  components: { CompanyHeader, ManagerNavigation, ManagerGreeting },

  data() {
    return {
      name: "",

      shiftInputs: {
        startDate: "",
        endDate: "",
        startTime: "",
        duration: 0,
      },
      listOfUsers: [],
      selectedUsers: [],
      userRole: "",
      isManager: false,
      listOfVacations: [{
        vacationId: 0,
        employeeId: 0,
        employeeName: '',
        startDate: '',
        endDate: '',
        status: 0,
        description: ''

      }]
    };
  },

  methods: {
    getAllUsers() {
      AuthService.getUsers().then((response) => {
        this.listOfUsers = response.data.map((user) => ({
          ...user,
          showShiftForm: false,
          hours: [],
        }));
      });
    },

    toggleSelection(userId) {
      const index = this.selectedUsers.indexOf(userId);
      if (index !== -1) {
        this.selectedUsers.splice(index, 1);
      } else {
        this.selectedUsers.push(userId);
      }
    },
    getFullName() {
      ShiftService.getUserFullName().then((response) => {
        this.name = response.data;

        this.$store.commit("ADD_NAME", this.name);
      
      });
    },

    isSelected(userId) {
      return this.selectedUsers.includes(userId);
    },

    showNewShiftAddedAlert(userId) {
      const listOfSelectedUsers = [];
      listOfSelectedUsers.push(userId);

      listOfSelectedUsers.forEach(userId => {

        alert(`Shift/s has been added to employee ${userId}`);
      })
    },

    isShiftBetweenVacation(user){
      
      let x = 0;
      let start = new Date(this.shiftInputs.startDate);
      let end = start;
      
     this.listOfVacations.forEach(e => {
      let startVacation = new Date(e.startDate);
      let endVacation = new Date(e.endDate);
     

      if(e.employeeId == user.id)
      if((start>= startVacation && start <= endVacation) || (end >= startVacation && end <= endVacation) || (start<= startVacation && end >= endVacation) ){
        x = 1;
      }

     })
     if(x==1){
      return true;
     }
    },


    getListOfVacations(){
      ManagerService.getListOfVacations(2).then(response => {

            this.listOfVacations = response.data;
      }
      )
    },

    submitShifts() {

    
      let x = 0;
      this.selectedUsers.forEach((userId) => {
        let startDate = new Date(this.shiftInputs.startDate);
        const endDate = this.shiftInputs.endDate // if end date isnt specified, end date is a copy of start date so the loop runs once
          ? new Date(this.shiftInputs.endDate)
          : new Date(startDate); 

        if(startDate>endDate){ //check if start date is greater then endDate then alert
           alert("Error submitting this request check start date");
          return;
        }
        if(this.shiftInputs.duration>8){
           alert("Duration must be 8 hours")
          return;
          
        }

        while (startDate <= endDate) {
          let startDateTime = new Date(startDate);
          startDateTime.setHours(...this.shiftInputs.startTime.split(":"));
          const offset = startDateTime.getTimezoneOffset()
          startDateTime = new Date(startDateTime.getTime() - (offset*60*1000))

          const newShift = {
            assignedId: userId,
            startDateTime: startDateTime.toISOString(),
            duration: this.shiftInputs.duration,
            status: 1,
            emergency: false,
            covererId: userId,
            description: `Shift for ${startDateTime.toLocaleDateString()}`,
          };

          ShiftService.createShift(newShift)
          .then((response) => {
            if (response.status === 201) {
              if (x == 0) {
                this.showNewShiftAddedAlert(userId);
                x++;
              }
            }else{
              alert("The shift could not be submitted");
              
            }

          })
          .catch(err => {
            alert("The shift could not be submitted");
            
          });

          startDate.setDate(startDate.getDate() + 1);

        }
      });

      this.shiftInputs = {};
      this.selectedUsers = [];
    },

    getFirstDayOfWeek(date) {
      const newDate = new Date(date);
      const first = newDate.getDate() - newDate.getDay();
      const firstDay = new Date(newDate.setDate(first));
      return firstDay.toISOString().split('T')[0];
    },

    ShiftNotifications() {
      ShiftService.getShifts().then((response) => {
        const shifts = response.data;
        const now = new Date();
        const deadline = new Date(now.getTime() + (48 * 60 * 60 * 1000));


        const filteredShifts = shifts.filter((shift) => {
          const shiftDate = new Date(shift.startDateTime);

          return shift.status == 3 && shiftDate <= deadline && shiftDate >= now;
        });


        if (filteredShifts.length > 0) {
          // Show a single alert with the count of uncovered shifts
          alert(`There are ${filteredShifts.length} uncovered shift(s) upcoming.`);
        }
      });
    },

    cleanup(){
      ShiftService.getCleanup().then(response => {

        response.data;
      })
    },

  },

  computed: {

    dateNow() {
      const today = new Date(); //current date and time object
      const year = today.getFullYear(); // get year based on date
      const month = String(today.getMonth() + 1).padStart(2, '0'); // get month
      const day = String(today.getDate()).padStart(2, '0'); //get day

      return `${year}-${month}-${day}`;
    },

    startAndEnd() { // computed property for if the start or end changes, used for watcher
      return [this.shiftInputs.startDate, this.shiftInputs.endDate];
    }
  },

  created() {
    this.cleanup();
    this.getAllUsers();
    this.getFullName();
    this.ShiftNotifications();
    this.getListOfVacations();

   // setInterval(this.checkUncoveredShifts, 60 * 60 * 1000);  //to check every Hour
    // setInterval(this.checkUncoveredShifts, 60 * 60 * 1000);  //to check every Hour

    ///////
    // this.userRole = this.$store.state.user.authorities[0].name;
    // this.isManager = this.userRole === "ROLE_MANAGER";
    // if (!this.isManager) {
    //   this.$router.push("/login"); // this to redirect to login/register page
    // } else {
    //   this.getAllUsers();
    //   this.getFullName();
    // }
  },

  watch: {
    startAndEnd() {
      if(this.shiftInputs.startDate == ""){ // if no start date, set hours to blank and return
        for(const user of this.listOfUsers){
          user.hours = [];
        }
        return;
      }
      let startDate = new Date(this.shiftInputs.startDate);
      const endDate = this.shiftInputs.endDate // if end date is not specified, end date is a copy of start date so the loop runs once
        ? new Date(this.shiftInputs.endDate)
        : new Date(startDate); 
      let weeks = [];

      while (startDate <= endDate) { // get all unique starts of week in a date range
        let firstDay = this.getFirstDayOfWeek(startDate)
        if(weeks.indexOf(firstDay) == -1){
          weeks.push(firstDay);
        }
        startDate.setDate(startDate.getDate() + 1);
      }
      // console.log(weeks);

      for(const user of this.listOfUsers){
        user.hours = [];
        for(const w of weeks){ // for all unique starts of weeks, show hours worked for that week
          ShiftService.getHoursWorkedByUserId(user.id, w).then(response => {
            user.hours.push(w +  ": " + response.data);
          })
        }
      }
    }
  }
};


</script>
  
<style scoped>
#data {
  display: inline-block;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.together {
  display: flex;
  width: 100%;
  margin: 20px;
  justify-content: space-evenly;
}

.bubble {
  background-color: #4a90e2;
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

.bubble-title {
  font-weight: bold;
  flex: 1 1 auto;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
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

.add-shift-button {
  border: none;
  border-radius: 20px;
  padding: 20px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-left: auto;
}

.add-button {
  background-color: #5cb85c;
  /* Green */
}

.add-button:hover {
  background-color: #4cae4c;
  /* Darker green */
}

.cancel-button {
  background-color: #d9534f;
  /* Red */
}

.cancel-button:hover {
  background-color: #c9302c;
  /* Darker red */
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

#shift-inputs {
  margin: 0 auto;
  width: 80%;
  max-width: fit-content;
  position: relative;
  z-index: 2;
  /* Ensure it is above the overlay but below scrolling content */
}

.filter {
  background-color: orange;
  color: white;
  border-radius: 50px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  text-align: center;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
  gap: 20px;
}

input[type="text"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

input[type="date"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

input[type="time"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

input[type="number"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

.submit-button {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

#backImage {
  position: relative;
  height: 100vh;
  background-image: url("../assets/arlington-research-kN_kViDchA0-unsplash (1).jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  background-attachment: fixed; /* Fixed by default */
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  z-index: 1;
  /* Less than header */
}


.grey{

  color: red;

}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: white;
  z-index: 2;
}

.scrollable-container {
  position: fixed;
  top: 20em;
  /* Adjust this based on your header height */
  left: 0;
  right: 0;
  bottom: 0;
  overflow: auto;
  z-index: 1; /* Less than header */
}

.scrollable-content {
  height: 100%;
  overflow: auto;
  padding: 10px;
}

.content {
  position: relative;
  z-index: 1; /* Make sure it's behind the fixed header */
}

section {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

@media (max-width: 600px) {

section {
  
  overflow: auto; /* Enable scrolling */
  
}

.fixed-header {
  position: relative;
  overflow: auto;
  height: 100vh;
}


.scrollable-container {
  position: relative; /* Adjust positioning to allow scrolling */
  overflow: auto; /* Allow scrolling */
  top: 0; /* Reset top position */
  padding-bottom: 120px;
  margin-bottom: 50px;
}
#search-shifts {
  display: flex;
  width: 65%; /* Make the search shifts section take more width on smaller screens */
  padding: 20px;
  z-index: 2;
}

.filter {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
}

input[type="text"], input[type="date"], select, [type="button"] {
  width: 100%;
  height: auto; /* Adjust height for better fit */
  margin-bottom: 2px; /* Add margin for spacing between elements */
}

.bubble {
  flex-direction: column; /* Stack elements vertically in smaller screens */
  padding: 20px;
}

.bubble-title {
  font-size: 16px; /* Smaller text size */
  padding: 10px;
}

#backImage {
background-attachment: scroll;
background-repeat: repeat;
background: transparent;
height: 100%;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  z-index: 1;
  overflow: auto;
}
}

/* For even smaller screens */
@media (max-width: 400px) {

section {
  overflow: auto; /* Enable scrolling */
  height: 100%;
}

.content {
  position: relative;
  z-index: 1; /* Make sure it's behind the fixed header */
  overflow: auto;
}

.fixed-header {
  overflow: auto;
}
.bubble-title {
  font-size: 14px; /* Further reduce text size */
}

.filter input[type="text"], 
.filter input[type="date"], 
.filter select, 
.filter [type="button"] {
  font-size: 16px; /* Adjust font size for better readability */
}

#backImage {
  background-attachment: scroll;
  background-repeat: repeat; /* Ensure scroll behavior on very small screens */
  overflow: auto;
  background: transparent;
  height: 100%;
}

.overlay {
position: absolute;
top: 0;
left: 0;
width: 100%;
height: 100%;
background: transparent;
z-index: 0;
overflow: auto;
}

.scrollable-container {
  position: relative; /* Adjust positioning to allow scrolling */
  overflow: auto; /* Allow scrolling */
  top: 0; /* Reset top position */
  padding-bottom: 120px;
}
}
</style>

  