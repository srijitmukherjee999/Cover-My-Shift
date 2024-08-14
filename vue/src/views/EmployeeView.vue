<template>
  <section>
    <div class="fixed-header">
      <company-header />
      <employee-greeting />
      <div id="backImage">
        <div class="overlay"></div>
        <div class="content">
          <employee-navigation />

          <div id="search-shifts">
            <div class="filter">
              <input
                type="text"
                id="assignedNameFilter"
                placeholder="Name"
                v-model="filter.assignedName"
              />
              <input
                type="date"
                id="startDateTime"
                v-model="filter.startDateTime"
              />
              <input
                type="text"
                id="duration"
                v-model="filter.duration"
                placeholder="Duration"
              />
              <select id="myList1" v-model="filter.status">
                <option id="status">--None--</option>
                <option value="uncovered request">Uncovered Request</option>
                <option value="covered">Covered</option>
                <option value="uncovered">Uncovered</option>
                <option value="assigned">Assigned</option>
              </select>

              <select id="myList2" v-model="filter.emergency">
                Emergency
                <option id="emergency">--None--</option>
                <option value="true">true</option>
                <option value="false">false</option>
              </select>
              <button type="button" @click="clearForm">Clear</button>
            </div>
          </div>
        </div>

        <div class="scrollable-container" v-if="done">
          <div class="scrollable-content">
            <div class="content">
              <div id="data" v-for="shift in filteredList" :key="shift.shiftId">
                <div class="together">
                  <div
                    class="bubble"
                    :class="{
                      emergency: shift.emergency && shift.status == 3,
                      green: shift.status == 4 || shift.status == 1,
                    }"
                  >
                    <div id="shiftObjects1">
                      <p class="bubble-title">{{ shift.assignedName }}</p>
                    </div>

                    <div id="shiftObjects2">
                      <p class="bubble-title">
                        {{ formatDate(shift.startDateTime) }}
                      </p>
                    </div>

                    <div id="shiftObjects3">
                      <p class="bubble-title">
                        {{ shift.duration }} <span>Hours</span>
                      </p>
                    </div>

                    <div id="shiftObjects4">
                      <p class="bubble-title">
                        Status: {{ convertStatus(shift.status) }}
                      </p>
                    </div>

                    <div id="shiftObjects5">
                      <button
                        class="button-title"
                        @click="updateShiftStatusToUncovered(shift.shiftId)"
                        v-if="shift.assignedName == name && shift.status === 1"
                      >
                        Request Day Off
                      </button>
                    </div>
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
import CompanyHeader from "../components/CompanyHeader.vue";
import EmployeeGreeting from "../components/EmployeeGreeting.vue";
import EmployeeNavigation from "../components/EmployeeNavigation.vue";
import ShiftService from "../services/ShiftService";

export default {
  components: { CompanyHeader, EmployeeGreeting, EmployeeNavigation },

  data() {
    return {
      listOfShifts: [
        {
          assignedName: "",
          shiftId: 0,
          assigned: 0,
          startDateTime: "",
          duration: 0,
          status: 0,
          emergency: false,
          coverer: 0,
          covererName: "",
          description: "",
        },
      ],
      done: false,
      name: "",
      filter: {
        assignedName: "",
        startDateTime: "",
        duration: "",
        status: "--None--",
        emergency: "--None--",
      },
      userRole: "",
      isEmployee: false,
    };
  },
  methods: {
    getAllShifts() {
      ShiftService.getShifts().then((response) => {
        this.listOfShifts = response.data;
        this.done = true;
      });
    },
    getFullName() {
      ShiftService.getUserFullName().then((response) => {
        this.name = response.data;

        this.$store.commit("ADD_NAME", this.name);
      });
    },

    updateShiftStatusToUncovered(shiftId) {
      ShiftService.updateShiftStatus(shiftId, 2).then((response) => {
        if (response.status === 200) {
          alert("You have requested the day off. Pending Management decision");
          this.getAllShifts();
        }
      });
    },

    convertStatus(status) {
      if (status == 1) return "assigned";
      if (status == 2) return "uncovered request";
      if (status === 3) return "uncovered";
      if (status == 4) return "covered";
    },
    cleanup(){
      ShiftService.getCleanup().then(response => {

        response.data;
      })
    },

    convertStatusToNumber(status) {
      if (status.toLowerCase().includes("assigned")) return 1;
      if (status.includes("uncovered request")) return 2;
      if (status.includes("uncovered")) return 3;
      if (status.includes("covered")) return 4;
    },
    convertStringToBoolean(emergency) {
      if (emergency === "true") return true;
      if (emergency === "false") return false;
    },
    convertDateToString(startDateTime) {},
    clearForm() {
      this.filter = {
        assignedName: "",
        startDateTime: "",
        duration: "",
        status: "--None--",
        emergency: "--None--",
      };
    },
    formatDate(dateTime) {
      const options = {
        weekday: 'long', // "Monday"
        year: 'numeric', // "2024"
        month: 'long', // "August"
        day: 'numeric', // "20"
        hour: 'numeric', // "4 PM"
        minute: 'numeric', // "00"
        hour12: true, // Use 12-hour time format
      };
      return new Date(dateTime).toLocaleString('en-US', options);
    },
  },

  created() {
    this.cleanup();
    this.getAllShifts();
    this.getFullName();

    ////////
    // this.userRole = this.$store.state.user.authorities[0].name;
    // this.isEmployee = this.userRole === "ROLE_EMPLOYEE";
    // if (!this.isEmployee) {
    //   this.$router.push("/login"); // this to redirect to login/register page
    // } else {
    //   this.getAllShifts();
    //   this.getFullName();
    // }
  },

  computed: {
    shiftDetails() {
      const shiftId = this.$route.params.id;
      return this.listOfShifts.find((e) => e.shiftId === shiftId);
    },
    filteredList() {
      let filteredUsers = this.listOfShifts;
      if (this.filter.assignedName != "") {
        filteredUsers = filteredUsers.filter((shift) =>
          shift.assignedName
            .toLowerCase()
            .includes(this.filter.assignedName.toLowerCase())
        );
      }
      if (this.filter.startDateTime != "") {
        filteredUsers = filteredUsers.filter((shift) =>
          shift.startDateTime.includes(this.filter.startDateTime)
        );
      }
      if (this.filter.duration != 0) {
        filteredUsers = filteredUsers.filter(
          (shift) => shift.duration == this.filter.duration
        );
      }
      if (this.filter.status != "--None--") {
        filteredUsers = filteredUsers.filter(
          (shift) =>
            this.convertStatus(shift.status) == this.filter.status.toLowerCase()
        );
      }
      if (this.filter.emergency != "--None--") {
        filteredUsers = filteredUsers.filter(
          (shift) =>
            shift.emergency ===
            this.convertStringToBoolean(this.filter.emergency)
        );
      }

      return filteredUsers;
    },

    // currentDate(){   //  :min="currentDate" add this date in the input
    //   const today = new Date(); //current date and time object
    //   const year = today.getFullYear(); // get year based on date
    //   const month = String(today.getMonth() + 1).padStart(2, '0'); // get month
    //   const day =String(today.getDate()).padStart(2, '0'); //get day

    //   return `${year}-${month}-${day}`;
    // },
    // userRole() {
    //   return this.$store.state.user.authorities[0].name; // Adapt this based on your state management
    // },
  },
};
</script>

<style scoped>
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
  max-width: 100%;
  width: auto;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  text-align: left;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
}

.together {
  display: flex;
  justify-content: center;
  width: 100%;
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

.bubble-title {
  font-weight: bold;
  flex: 1 1 auto;
  padding: 15px;
  margin: 10px;
  border-radius: 5px;
  text-align: center;
}

.bubble p {
  margin: 0;
  padding: 0;
  margin-right: 20px;
}

#shiftObjects5 {
  width: 150px;
  height: 5px;
  max-width: 100%;
  width: auto;
}

#shiftObjects5:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.button-title {
  font-weight: bold;
  flex: 1 1 auto;
  border-radius: 5px;
  text-align: center;
}

.emergency {
  background-color: red;
  text-decoration: underline;
  animation: vertical-shaking 4s infinite;
}
.green {
  background-color: green;
  text-decoration: green;
}

#search-shifts {
  position: relative;
  width: 68%; /* Adjust width as needed */
  margin: auto;
  padding: 10px;
  background-color: orange;
  color: white;
  border-radius: 50px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 10; /* Ensure it's on top of other elements */
}

.filter {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  text-align: center;
  gap: 10px; /* Adjust gap as needed */
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

[type="button"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

[type="button"]:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  background-color: lightgray;
}

#myList1,
#myList2 {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}
.myFilter {
  justify-content: center;
  align-content: center;
}

@keyframes vertical-shaking {
  0% {
    transform: translateY(0);
  }
  25% {
    transform: translateY(5px);
  }
  50% {
    transform: translateY(-5px);
  }
  75% {
    transform: translateY(5px);
  }
  100% {
    transform: translateY(0);
  }
}

h1 {
  font-style: italic;
  font-weight: bold;
  animation: fadeIn 4s;
}

#backImage {
  position: relative;
  height: 100vh;
  background-image: url("..\assets\nastuh-abootalebi-eHD8Y1Znfpk-unsplash.jpg");
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
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: rgb(255, 255, 255);
  z-index: 2;
}

.scrollable-container {
  position: fixed;
  top: 19em; /* Adjust this based on your header height */
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
    padding: 5px;
  }

  input[type="text"], input[type="date"], select, [type="button"] {
    width: 100%;
    height: auto; /* Adjust height for better fit */
    margin-bottom: 2px; /* Add margin for spacing between elements */
  }

  .bubble {
    flex-direction: column; /* Stack elements vertically in smaller screens */
    padding: 15px;
  }

  .bubble-title {
    font-size: 16px; /* Smaller text size */
    padding: 10px;
  }

  #backImage {
  background-attachment: scroll;
  background-repeat: repeat;
  background: transparent;
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

  .scrollable-container {
    position: relative; /* Adjust positioning to allow scrolling */
    overflow: auto; /* Allow scrolling */
    top: 0; /* Reset top position */
    padding-bottom: 120px;
  }
}
</style>

