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
                <input
                  type="date"
                  v-model="shiftInputs.startDate"
                  placeholder="Start Date"
                />
              </div>
              <div>
                <input
                  type="date"
                  v-model="shiftInputs.endDate"
                  placeholder="End Date (optional)"
                />
              </div>
              <div>
                <input
                  type="time"
                  v-model="shiftInputs.startTime"
                  placeholder="Start Time"
                />
              </div>
              <div>
                <input
                  type="number"
                  v-model="shiftInputs.duration"
                  placeholder="Duration (hours)"
                />
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
              <div class="together">
                <div class="bubble">
                  <div class="bubble-title">
                    <p>{{ user.fullName }}</p>
                  </div>
                  <div>
                    <p v-bind:key="user.hours">{{ user.hours }}</p>
                  </div>
                  <div>
                    <button
                      :class="[
                        'add-shift-button',
                        isSelected(user.id) ? 'selected-button' : 'add-button',
                      ]"
                      @click="toggleSelection(user.id)"
                    >
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
    };
  },

  computed: {
    startAndEnd() { // computed property for if the start or end changes, used for watcher
      return [this.shiftInputs.startDate, this.shiftInputs.endDate];
    }
  },

  methods: {
    getAllUsers() {
      AuthService.getUsers().then((response) => {
        this.listOfUsers = response.data.map((user) => ({
          ...user,
          showShiftForm: false,
          hours: "",
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

    showNewShiftAddedAlert(userId){
       const listOfSelectedUsers = [];
       listOfSelectedUsers.push(userId);

      listOfSelectedUsers.forEach(userId => {

        alert(`Shift/s has been added to employee ${userId}`);
      })
    },

    submitShifts() {
     
      let x = 0 ;
      this.selectedUsers.forEach((userId) => {
       
        let startDate = new Date(this.shiftInputs.startDate);
        const endDate = this.shiftInputs.endDate // if end date isnt specified, end date is a copy of start date so the loop runs once
          ? new Date(this.shiftInputs.endDate)
          : new Date(startDate); 

        while (startDate <= endDate) {
          const startDateTime = new Date(startDate);
          startDateTime.setHours(...this.shiftInputs.startTime.split(":"));

          const newShift = {
            assignedId: userId,
            startDateTime: startDateTime.toISOString(),
            duration: this.shiftInputs.duration,
            status: 1,
            emergency: false,
            covererId: userId,
            description: `Shift for ${startDateTime.toLocaleDateString()}`,
          };

          ShiftService.createShift(newShift).then((response) => {
            if (response.status === 201) {
              if(x == 0){
                this.showNewShiftAddedAlert(userId);
                x++;
              }
            }
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

  // ShiftNotifications() {
  //     ShiftService.getShifts().then((response) => {
  //       let count = 0;
  //       const shifts = response.data;
  //       const now = new Date();
  //       const deadline = new Date(now.getTime() + (48 * 60 * 60 * 1000));
  //       console.log(deadline); // print to check
  //       console.log("hello"); ///check if you show what is needed

  //       const filteredShifts = shifts.filter((shift) => { 
  //         const shiftDate = new Date(shift.startDateTime);
  //         console.log(shift.startDateTime);
  //         return shift.status == 3 && shiftDate <= deadline && shiftDate >= now;
  //       });
  //       console.log(filteredShifts.length);

  //       if (filteredShifts.length > 0) {  // Show notification alert for matching shifts
  //           filteredShifts.forEach((shift) => {
  //           const shiftDate = new Date(shift.startDateTime).toLocaleString();
  //           alert(`Uncovered Shift Reminder: Shift "${shift.description}" is scheduled on ${shiftDate}.`);
  //         });
  //       }
  //     });
  //   },

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
    }

  },

////////////////////////////////////////////////////////////////////////////////////////////////////////

  created() {
    this.getAllUsers();
    this.getFullName();
    this.ShiftNotifications();
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
          user.hours = ""
        }
        return;
      }
      let startDate = new Date(this.shiftInputs.startDate);
      const endDate = this.shiftInputs.endDate // if end date isnt specified, end date is a copy of start date so the loop runs once
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
      console.log(weeks);

      for(const user of this.listOfUsers){
        user.hours = "";
        for(const w of weeks){ // for all unique starts of weeks, show hours worked for that week
          ShiftService.getHoursWorkedByUserId(user.id, w).then(response => {
            user.hours += "Hours for week of " + w + ": " + response.data + "\n";
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

@media (max-width: 600px) {
  .container {
    flex-direction: column;
  }
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
  background-color: #5cb85c; /* Green */
}

.add-button:hover {
  background-color: #4cae4c; /* Darker green */
}

.cancel-button {
  background-color: #d9534f; /* Red */
}

.cancel-button:hover {
  background-color: #c9302c; /* Darker red */
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
  z-index: 2; /* Ensure it is above the overlay but below scrolling content */
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

section,
html {
  margin: 0;
  padding: 0;
  height: 100%;
  background: transparent;
}

#backImage {
  position: relative;
  height: 100vh;
  background-image: url("../assets/arlington-research-kN_kViDchA0-unsplash (1).jpg");
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
  z-index: 1; /* Less than header */
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background: white;
}

.scrollable-container {
  position: fixed;
  top: 20em; /* Adjust this based on your header height */
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 1; /* Less than header */
}

.scrollable-content {
  height: 100%;
  overflow-y: auto;
  padding: 10px;
}

.content {
  position: relative;
  z-index: 1; /* Make sure it's behind the fixed header */
}

</style>

  