<template>
  <section>
    <div class="fixed-header">
      <company-header />
      <employee-greeting />
      <div id="backImage">
        <div class="overlay"></div>
        <div class="content">
          <employee-navigation />
          <!-- <request-off-form /> -->
          <section>
            <div id="form-body">
              <form class="request-off-form" v-on:submit.prevent="addNewRequest" v-if="showForm">
                <input type="date" class="start-date-input" placeholder="Date" v-model="newRequest.startDate"
                  :min="dateNow">
                <input type="date" class="end-date-input" placeholder="Date" v-model="newRequest.endDate"
                  :min="dateNow">
                <input type="text" class="description-input" placeholder="description" v-model="newRequest.description">
                <button type="submit">Submit Request</button>
              </form>

              <button class="requestOffButton" v-else @click="toggleForm">Request Time Off</button>

            </div>
          </section>
          <h2>Your Pending Vacation Requests:</h2>
        </div>

        <div class="scrollable-container">
          <div class="scrollable-content">
            <div class="content">
              <div id="data" v-for=" vacation in listOfPendingVacationRequests" v-bind:key="vacation.vacationId">
                <div class="together">
                  <div class="bubble">
                    <div id="shiftObjects">
                      <p class="bubble-title"> {{ vacation.employeeName }}</p>
                    </div>

                    <div id="shiftObjects">
                      <p class="bubble-title">
                        {{ vacation.startDate }}
                      </p>
                    </div>

                    <div id="shiftObjects">
                      <p class="bubble-title">
                        {{ vacation.endDate }}
                      </p>
                    </div>

                    <div id="shiftObjects">
                      <p class="bubble-title">
                        Description: {{ vacation.description }}
                      </p>
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
  data() {
    return {
      name: "",
      listOfPendingVacationRequests: [],
      showForm: true,
      newRequest: {
        startDate: '',
        endDate: '',
        description: '',
        status: 1,
      },


    };
  },
  components: {

    CompanyHeader,
    EmployeeGreeting,
    EmployeeNavigation,
  },
  methods: {
    getFullName() {
      ShiftService.getUserFullName().then((response) => {
        this.name = response.data;

        this.$store.commit("ADD_NAME", this.name);
      });
    },

    getPendingVacationRequests() {
      ShiftService.getVacationByStatus(1, true).then(response => {

        this.listOfPendingVacationRequests = response.data;

      })
    },
    addNewRequest() {

      const vacationRequest = {
        employeeId: this.$store.state.user.id,
        startDate: this.newRequest.startDate,
        endDate: this.newRequest.endDate,
        status: 1,
        description: this.newRequest.description
      };

      ShiftService.createVacationRequest(vacationRequest).then(response => {
        if (response.status === 201) {
          alert("Vacation request submitted and is pending management review.");
          this.clearForm();
          this.getPendingVacationRequests();

        } else {
          alert("There was an error submitting your request.");
        }

      }).catch(error => {
        console.error("Error creating vacation request:", error);
        alert("There was an error submitting your request.");
      });
    },
    clearForm() {
      this.newRequest = {
        startDate: '',
        endDate: '',
        description: ''
      };
      this.showForm = false;
    },
    toggleForm() {
      this.showForm = !this.showForm;
    },


    convertStatus(status) {
      if (status == 1) return "assigned";
      if (status == 2) return "uncovered request";
      if (status === 3) return "uncovered";
      if (status == 4) return "covered";
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
  computed: {

    dateNow(){
      const today = new Date(); //current date and time object
      const year = today.getFullYear(); // get year based on date
      const month = String(today.getMonth() + 1).padStart(2, '0'); // get month
      const day = String(today.getDate()).padStart(2, '0'); //get day

      return `${year}-${month}-${day}`;
    },

  },

  created() {
    this.getFullName();
    this.getPendingVacationRequests();
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

.green {
  background-color: green;
  text-decoration: green;
}

.request-off-form {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

h2 {
  display: flex;
  justify-content: center;
  color: White;
  text-decoration-line: underline;
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
  top: 25em;
  /* Adjust this based on your header height */
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 1;
  /* Less than header */
}

.scrollable-content {
  height: 100%;
  overflow-y: auto;
  padding: 10px;
}

.content {
  position: relative;
  z-index: 1;
}

#form-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

form {
  display: flex;
  flex-direction: row;
  align-items: center;
  align-content: center;
  gap: 20px;
}

.request-off-form {
  background-color: orange;
  color: white;
  border-radius: 50px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-content: center;
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

button[type="submit"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

button:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  background-color: lightgray;
}

.requestOffButton {
  display: inline;
  margin: auto;
  font-size: larger;
  color: Orange;
  font-weight: bold;
  border-radius: 50px;
  padding: 20px;
  box-shadow: 0 4px 8px;
  max-width: 100%;
  transition: transform 0.3s, box-shadow 0.3s;
  font: bold;
  border-color: orange;
  border-width: 20px;
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

h2 {
  display: flex;
  justify-content: center;
  color: black;
  text-decoration-line: underline;
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

h2 {
  display: flex;
  justify-content: center;
  color: black;
  text-decoration-line: underline;
}
}

</style>