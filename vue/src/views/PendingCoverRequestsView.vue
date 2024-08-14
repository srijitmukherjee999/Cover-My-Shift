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
            <h2>Pending Cover Requests:</h2>

            <div id="data" v-for="request in coverRequests" :key="request.id">
              <div class="together">
                <div class="bubble">
                  <div>
                    <p class="bubble-title">
                      Request by: {{ request.fullName }}
                    </p>
                    <p>
                      Current hours: {{ request.hours }}
                    </p>
                    <p>
                      Hours on accept: {{ request.hours + this.shiftDuration }}
                    </p>
                  </div>
                  <div class="bubble-actions">
                    <button
                      class="accept-button"
                      @click="openPopup('accept', shiftId, request.id)"
                    >
                      Accept
                    </button>
                    <button
                      class="reject-button"
                      @click="openPopup('reject', shiftId, request.id)"
                    >
                      Reject
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Popup for message input -->
            <div v-if="showPopup" class="popup-overlay">
              <div class="popup-content">
                <h2>
                  {{ popupAction === "accept" ? "Accept" : "Reject" }} Cover
                  Request
                </h2>
                <textarea
                  v-model="message"
                  placeholder="Enter your message here..."
                ></textarea>
                <div class="popup-buttons">
                  <button @click="submitAction">Submit</button>
                  <button @click="closePopup">Cancel</button>
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
import ManagerGreeting from "../components/ManagerGreeting.vue";
import ManagerNavigation from "../components/ManagerNavigation.vue";
import ManagerService from "../services/ManagerService";
import ShiftService from "../services/ShiftService";

export default {
  data() {
    return {
      shiftId: this.$route.params.shiftId,
      shiftDuration: 0,
      shiftDate: null,
      coverRequests: [],
      message: "",
      showPopup: false,
      popupAction: "",
      selectedShiftId: null,
      selectedEmployeeId: null
    };
  },
  methods: {
    getCoverRequests() {
      ManagerService.getCoverRequests(this.shiftId, 1)
        .then((response) => {
          this.coverRequests = response.data;
          for(let req of this.coverRequests) {
            console.log(req)
            ShiftService.getHoursWorkedByUserId(req.id, this.shiftDate.split('T')[0]).then((response) => {console.log(response.data); req.hours = response.data})
          }
        })
        .catch((error) => {
          console.error("Error fetching cover requests:", error);
        });
    },
    openPopup(action, shiftId, employeeId) {
      this.popupAction = action;
      this.selectedShiftId = shiftId;
      this.selectedEmployeeId = employeeId;
      this.showPopup = true;
    },
    closePopup() {
      this.showPopup = false;
      this.message = "";
    },
    submitAction() {
      const approvalObject = this.createApprovalObject(
        this.selectedShiftId,
        this.selectedEmployeeId,
        this.popupAction === "accept"
      );
      ManagerService.acceptRejectRequest(approvalObject).then((response) => {
        if (response.status === 200) {
          alert(
            `${
              this.popupAction === "accept" ? "Accepted" : "Rejected"
            } shift with ID: ${this.selectedShiftId}`
          );
          this.getCoverRequests();
          this.closePopup();
        }
      });
    },
    createApprovalObject(shiftID, employeeId, isApproved) {
      return {
        shiftId: shiftID,
        employeeId: employeeId,
        approved: isApproved,
        message: this.message,
      };
    },
    getFirstDayOfWeek(date) {
      const newDate = new Date(date);
      const first = newDate.getDate() - newDate.getDay();
      const firstDay = new Date(newDate.setDate(first));
      return firstDay.toISOString().split('T')[0];
    },

    cleanup(){
      ShiftService.getCleanup().then(response => {

        response.data;
      })
    },
  },

  created() {
    this.cleanup();
    ShiftService.getShiftById(this.shiftId).then(response => {this.shiftDuration = response.data.duration; this.shiftDate = this.getFirstDayOfWeek(response.data.startDateTime); this.getCoverRequests();});
  },
  components: { CompanyHeader, ManagerGreeting, ManagerNavigation },
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

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-content {
  background: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 300px;
  text-align: center;
}

.popup-content textarea {
  width: 100%;
  height: 100px;
  margin-bottom: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  padding: 10px;
}

.popup-buttons {
  display: flex;
  justify-content: space-between;
}

.popup-buttons button {
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  font-weight: bold;
}

.popup-buttons .submit-button {
  background-color: #5cb85c;
  color: white;
}

.popup-buttons .submit-button:hover {
  background-color: #4cae4c;
}

.popup-buttons .cancel-button {
  background-color: #d9534f;
  color: white;
}

.popup-buttons .cancel-button:hover {
  background-color: #c9302c;
}

h2 {
  display: flex;
  justify-content: center;
  color: White;
  text-decoration-line: underline;
}

.container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
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
  top: 320px; /* Adjust this based on your header height */
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

