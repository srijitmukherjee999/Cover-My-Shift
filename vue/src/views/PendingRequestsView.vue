<template>
  <section>
    <div class="fixed-header">
      <company-header />
      <manager-greeting />
      <div id="backImage">
        <div class="overlay"></div>
        <div class="content">
          <manager-navigation />
        </div>
      </div>

      <div class="scrollable-container">
        <div class="scrollable-content">
          <div class="content">
            <!-- Shift Requests -->
            <div
              id="data"
              v-for="shift in listOfPendingRequests"
              v-bind:key="shift.shiftId"
            >
              <div class="together">
                <div class="bubble">
                  <div id="shiftObjects1" class="bubble-title">
                    <p>{{ shift.assignedName }}</p>
                  </div>
                  <div id="shiftObjects2" class="bubble-title">
                    <p>{{ formatDate(shift.startDateTime) }}</p>
                  </div>
                  <div id="shiftObjects3" class="bubble-title">
                    <p>{{ shift.duration }} hours</p>
                  </div>
                  <div id="shiftObjects4" class="bubble-title">
                    <p>{{ shift.emergency ? "Emergency" : "Regular" }}</p>
                  </div>
                  <div class="bubble-actions">
                    <button
                      class="accept-button"
                      @click="approveDayOffRequest(shift.shiftId)"
                    >
                      Accept
                    </button>
                    <button
                      class="reject-button"
                      @click="denyDayOffRequest(shift.shiftId)"
                    >
                      Reject
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Vacation Requests -->
            <div
              id="data"
              v-for="vacation in listOfVacationRequests"
              v-bind:key="vacation.vacationId"
            >
              <div class="together">
                <div class="bubble">
                  <div id="vacationObjects1" class="bubble-title">
                    <p>{{ vacation.employeeName }}</p>
                  </div>
                  <div id="vacationObjects2" class="bubble-title">
                    <p>{{ vacation.startDate }}</p>
                  </div>
                  <div id="vacationObjects3" class="bubble-title">
                    <p>{{ vacation.endDate }}</p>
                  </div>
                  <div id="vacationObjects4" class="bubble-title">
                    <p>{{ vacation.description }}</p>
                  </div>

                  <!-- Conditional Rendering based on Status -->
                  <div v-if="vacation.status === 2" class="bubble-status">
                    <p>Accepted Vacation</p>
                  </div>
                  <div v-else-if="vacation.status === 3" class="bubble-status">
                    <p>Rejected Vacation</p>
                  </div>
                  <div v-else class="bubble-actions">
                    <button
                      class="accept-button"
                      @click="approveVacationRequest(vacation.vacationId)"
                    >
                      Accept
                    </button>
                    <button
                      class="reject-button"
                      @click="denyVacationRequest(vacation.vacationId)"
                    >
                      Reject
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
import ShiftService from "../services/ShiftService";
import ManagerService from "../services/ManagerService";
import ManagerGreeting from "../components/ManagerGreeting.vue";
import ManagerNavigation from "../components/ManagerNavigation.vue";

export default {
  data() {
    return {
      name: "",
      listOfPendingRequests: [],
      listOfVacationRequests: [],
      vacationUsers: [],
    };
  },
  components: { CompanyHeader, ManagerGreeting, ManagerNavigation },
  methods: {
    getFullName() {
      ShiftService.getUserFullName().then((response) => {
        this.name = response.data;
        this.$store.commit("ADD_NAME", this.name);
      });
    },
    fetchVacationUsers() {
      this.listOfVacationRequests.forEach((vacation) => {
        ShiftService.getUserByUserId(vacation.employeeId).then((response) => {
          this.vacationUsers = response.data;
        });
      });
    },
    getShiftPendingRequests() {
      ShiftService.getShiftsByUncoveredRequest(2).then((response) => {
        this.listOfPendingRequests = response.data;
      });
    },
    getVacationRequests() {
      ManagerService.getVacationRequests().then((response) => {
        this.listOfVacationRequests = response.data;
      });
    },
    approveDayOffRequest(id) {
      ManagerService.acceptDayOffRequest(id, 3)
        .then((response) => {
          if (response.status === 200) {
            alert("Accepted");
            this.getShiftPendingRequests();
          }
        })
        .catch((error) => {
          console.error("Error approving day off request:", error);
        });
    },
    denyDayOffRequest(id) {
      ManagerService.acceptDayOffRequest(id, 1)
        .then((response) => {
          if (response.status === 200) {
            alert("Rejected");
            this.getShiftPendingRequests();
          }
        })
        .catch((error) => {
          console.error("Error denying day off request:", error);
        });
    },
    approveVacationRequest(vacationId) {
      ManagerService.acceptRejectVacationRequest(vacationId, 2)  // 2 for accept
          .then((response) => {
              if (response.status === 200) {
                  alert("Vacation Request Accepted");
                  this.getVacationRequests();
              }
          })
          .catch((error) => {
              console.error("Error approving vacation request:", error);
          });
    },
    denyVacationRequest(vacationId) {
      ManagerService.acceptRejectVacationRequest(vacationId, 3)  // 3 for reject
          .then((response) => {
              if (response.status === 200) {
                  alert("Vacation Request Rejected");
                  this.getVacationRequests();
              }
          })
          .catch((error) => {
              console.error("Error rejecting vacation request:", error);
          });
    },
    createApprovalObject(vacationId, incomingStatus) {
      return {
        vacationId: vacationId,
        status: incomingStatus,
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
    this.getFullName();
    this.getShiftPendingRequests();

    ManagerService.getVacationRequests().then((response) => {
      this.listOfVacationRequests = response.data;
      this.fetchVacationUsers();
    });
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

.bubble-title {
  font-weight: bold;
  flex: 1 1 auto;
  padding: 15px;
  margin: 10px;
  border-radius: 5px;
  text-align: center;
  font-weight: bold;
}

.bubble-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.bubble-actions {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.accept-button {
  background-color: #5cb85c;
  border: none;
  border-radius: 50px;
  padding: 20px;
  cursor: pointer;
  color: white;
  font-weight: bold;
  gap: 20px;
  margin: 10px;
}

.accept-button:hover {
  background-color: #4cae4c;
}

.reject-button {
  background-color: #d9534f;
  border: none;
  border-radius: 50px;
  padding: 20px;
  cursor: pointer;
  color: white;
  font-weight: bold;
  gap: 20px;
  margin: 10px;
}

.reject-button:hover {
  background-color: #c9302c;
}

.container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
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
  top: 15em; /* Adjust this based on your header height */
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

.bubble-status {
  font-weight: bold;
  flex: 1 1 auto;
  padding: 15px;
  margin: 10px;
  border-radius: 5px;
  text-align: center;
  color: white;
}

.bubble-status p {
  margin: 0;
  font-size: 16px;
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

.scrollable-container {
  position: relative; /* Adjust positioning to allow scrolling */
  overflow: auto; /* Allow scrolling */
  top: 0; /* Reset top position */
  padding-bottom: 120px;
}
}
</style>
