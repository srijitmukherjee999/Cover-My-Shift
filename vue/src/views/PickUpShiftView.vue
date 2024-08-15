<template>
  <section>
    <div class="fixed-header">
      <company-header />
      <employee-greeting />
      <div id="backImage">
        <div class="overlay"></div>
        <div class="content">
          <employee-navigation />

          <div id="emergency-button">
            <div>
              <button
                class="emergencyButton"
                v-if="showButton"
                @click="getShiftsByEmergency"
              >
                EMERGENCY SHIFTS
              </button>
              <button class="clearButton" v-else @click="toggleButton">
                Clear
              </button>
            </div>

            
              
                <div id="emergency-bubbles"
                  class="emergencybubble"
                  v-for="emergency in emergencyShifts"
                  v-bind:key="emergency.shiftId">
                  <div class="together">
                  <div id="shiftObjects1">
                    <p class="bubble-title">
                      Name: {{ emergency.assignedName }}
                    </p>
                  </div>
                  <div id="shiftObjects2">
                    <p class="bubble-title">
                      Start Time: {{ emergency.startDateTime }}
                    </p>
                  </div>
                  <div id="shiftObjects3">
                    <p class="bubble-title">
                      Duration: {{ emergency.duration }}
                    </p>
                  </div>
                  <div id="shiftObjects4">
                    <p class="bubble-title">
                      {{ convertStatus(emergency.status) }}
                    </p>
                  </div>
                </div>
              </div>
            
          </div>
        </div>
      </div>

      <div class="scrollable-container">
        <div class="scrollable-content">
          <div class="content">
            <div id="data" v-if="showButton" v-for="shift in listOfShiftsByStatus" v-bind:key="shift">
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

                  <div id="shiftObjects6">
                    <button
                      class="button-title"
                       :disabled="isShiftCovered(shift.shiftId)" 
                      @click="coverThisShift(shift.shiftId)"
                    >
                      Cover This Shift
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
import CompanyHeader from "../components/CompanyHeader.vue";
import EmployeeGreeting from "../components/EmployeeGreeting.vue";
import EmployeeNavigation from "../components/EmployeeNavigation.vue";
import ShiftService from "../services/ShiftService";

export default {
  components: { CompanyHeader, EmployeeGreeting, EmployeeNavigation },

  data() {
    return {
      showButton: true,
      listOfShiftsByStatus: [],
      name: "",
      emergencyShifts: [],
      coverRequests: [],
    };
  },
  methods: {
    getShifts(status) {
      ShiftService.getShiftByStatus(status).then((response) => {
        this.listOfShiftsByStatus = response.data;
      });
    },
    convertStatus(status) {
      if (status == 1) return "assigned";
      if (status == 2) return "uncovered request";
      if (status === 3) return "uncovered";
      if (status == 4) return "covered";
    },

    getFullName() {
      ShiftService.getUserFullName().then((response) => {
        this.name = response.data;
        this.$store.commit("ADD_NAME", this.name);
      });
    },
    getShiftsByEmergency() {
      ShiftService.getEmergencyShifts(true, 3).then((response) => {
        this.emergencyShifts = response.data;
        this.showButton = false;
      });
    },
    cleanup(){
      ShiftService.getCleanup().then(response => {

        response.data;
      })
    },

    deleteShift(shiftId) {
      ShiftService.deleteUserShift(shiftId)
        .then((response) => {
          if (response.status === 200) {
            this.listOfShiftsByStatus = this.listOfShiftsByStatus.filter(
              (shift) => shift.shiftId !== shiftId
            );
          } else {
            console.error("Unexpected response status:", response.status);
          }
        })
        .catch((error) => {
          console.error("Error deleting this shift:", error);
        });
    },
    toggleButton() {
      this.showButton = !this.showButton;
      this.emergencyShifts = [];
    },
    formatDate(dateTime) {
      const options = {
        weekday: "long",
        year: "numeric",
        month: "long",
        day: "numeric",
        hour: "numeric",
        minute: "numeric",
        hour12: true,
      };
      return new Date(dateTime).toLocaleString("en-US", options);
    },
    coverThisShift(shiftId) {
      ShiftService.createCoverRequest(shiftId).then((response) => {
        if (response.status === 201) {
          alert(
            "You have requested to cover this shift. Pending Management decision"
          );
          this.getShifts(3);
          this.getCoverRequests();
        }
      });
    },
    getCoverRequests() {
      ShiftService.getCoverRequestByCovererId().then((response) => {
        this.coverRequests = response.data;
      });
    },
    isShiftCovered(shiftId) {
      return this.coverRequests.some((request) => request.shiftId === shiftId);
    },

  },

  created() {
    this.cleanup();
    this.getShifts(3);
    this.getFullName();
    this.getCoverRequests();
  },
};
</script>

<style scoped>
section {
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
  max-width: 100%;
  width: auto;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  text-align: left;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
}

.bubble:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

#shiftObjects5 {
  width: 150px;
  height: 5px;
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
  font-weight: bold;
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

.emergency {
  background-color: red;
  text-decoration: underline;
  animation: vertical-shaking 4s infinite;
}

.emergencyButton {
  display: flex;
  justify-content: center;
}

h1 {
  font-style: italic;
  font-weight: bold;
  animation: fadeIn 2s;
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

#emergency-button {
  padding: 20px;
  margin: 20px;
  text-align: center;
  border-radius: 5px;
  border-color: red;
}

#emergency-bubbles {
  display: inline-block;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px;
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

#emergency-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: rgb(255, 255, 255);
  /* Ensure visibility if needed */
}

.scrollable-container {
  position: fixed;
  top: 22em; /* Adjust this based on your header height */
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

@media (max-width: 600px) {

section {
  
  overflow: auto; /* Enable scrolling */
  
}

.fixed-header {
  position: relative;
  overflow: auto;
  height: 100%;
}


.scrollable-container {
  position: relative; /* Adjust positioning to allow scrolling */
  overflow: auto; /* Allow scrolling */
  top: 0; /* Reset top position */
  padding-bottom: 120px;
  margin-bottom: 50px;
}

.bubble {
  flex-direction: column; /* Stack elements vertically in smaller screens */
  padding: 15px;
  top: 5px;
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
}

.fixed-header {
  overflow: auto;
}
.bubble-title {
  font-size: 14px; /* Further reduce text size */
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