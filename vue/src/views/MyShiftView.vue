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
                type="date"
                id="startDateTime"
                v-model="myFilter.startDateTime"
              />
              <input
                type="text"
                id="duration"
                v-model="myFilter.duration"
                placeholder="Duration"
              />
              <select id="myList" v-model="myFilter.status">
                <option id="">--None--</option>
                <option value="uncovered request">Uncovered request</option>
                <option value="covered">Covered</option>
                <option value="uncovered">Uncovered</option>
                <option value="assigned">Assigned</option>
              </select>

              <select id="myList" v-model="myFilter.emergency">
                Emergency
                <option id="emergency">--None--</option>
                <option value="true">true</option>
                <option value="false">false</option>
              </select>
              <button type="button" @click="clearForm">Clear</button>
            </div>
          </div>
        </div>

        <div class="scrollable-container">
          <div class="scrollable-content">
            <div class="content">
              <div
                id="data"
                v-for="shift in filteredMyList"
                :key="shift.shiftId"
              >
                <div class="together">
                  <div class="bubble" :class="{ emergency: shift.emergency }">
                    <div id="shiftObjects1">
                      <p class="bubble-title">Name: {{ shift.assignedName }}</p>
                    </div>

                    <div id="shiftObjects2">
                      <p class="bubble-title">
                        Start Time: {{ shift.startDateTime }}
                      </p>
                    </div>

                    <div id="shiftObjects3">
                      <p class="bubble-title">
                        Duration: {{ shift.duration }} <span>hours</span>
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
      name: "",
      listOfMyShifts: [
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
      myFilter: {
        
        startDateTime: "",
        duration: "",
        status: "--None--",
        emergency: "--None--",
      },
    };
  },
  methods: {
    getMyShifts() {
      ShiftService.getMyShifts(true).then((response) => {
        this.listOfMyShifts = response.data;
      });
    },
    convertStatus(status) {
      if (status == 1) return "assigned";
      if (status == 2) return "uncovered request";
      if (status === 3) return "uncovered";
      if (status == 4) return "covered";
    },
    updateShiftStatusToUncovered(shiftId) {
      ShiftService.updateShiftStatus(shiftId, 2).then((response) => {
        if (response.status === 200) {
          alert("You have requested the day off. Pending Management decision");
          this.getMyShifts();
        }
      });
    },
    getFullName() {
      ShiftService.getUserFullName().then((response) => {
        this.name = response.data;

        this.$store.commit("ADD_NAME", this.name);
      });
    },
    convertStringToBoolean(emergency) {
      if (emergency === "true") return true;
      if (emergency === "false") return false;
    },
    clearForm() {
      this.myFilter = {
        assignedName: "",
        startDateTime: "",
        duration: "",
        status: "--None--",
        emergency: "--None--",
      };
    },
  },
  computed: {
    filteredMyList() {
      let filteredUsers = this.listOfMyShifts;

      if (this.myFilter.startDateTime != "") {
        filteredUsers = filteredUsers.filter((shift) =>
          shift.startDateTime.includes(this.myFilter.startDateTime)
        );
      }
      if (this.myFilter.duration != 0) {
        filteredUsers = filteredUsers.filter(
          (shift) => shift.duration == this.myFilter.duration
        );
      }
      if (this.myFilter.status != "--None--") {
        filteredUsers = filteredUsers.filter(
          (shift) =>
            this.convertStatus(shift.status) ==
            this.myFilter.status.toLowerCase()
        );
      }
      if (this.myFilter.emergency != "--None--") {
        filteredUsers = filteredUsers.filter(
          (shift) =>
            shift.emergency ===
            this.convertStringToBoolean(this.myFilter.emergency)
        );
      }

      return filteredUsers;
    },
  },
  created() {
    this.getMyShifts();
    this.getFullName();
  },
};
</script>


<style scoped>
#search-shifts {
  margin: 0 auto;
  width: 80%;
  max-width: fit-content;
  padding: 20px;
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

#myList {
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

h1 {
  font-style: italic;
  font-weight: bold;
  animation: fadeIn 2s;
}

#backImage {
  position: relative;
  height: 100vh;
  background-image: url("..\assets\nastuh-abootalebi-eHD8Y1Znfpk-unsplash.jpg"); /* Example image */
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}

/* Overlay with semi-transparent color */
.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8); /* Semi-transparent black overlay */
  z-index: 0; /* Place the overlay above the background but below content */
}

.content {
  position: relative;
  z-index: 1; /* Ensure content is above the overlay */
  padding: 20px; /* Add padding as needed */
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
  flex-direction: row; /* Aligns items horizontally */
  flex-wrap: wrap; /* Allows items to wrap to the next line */
}

@media (max-width: 600px) {
  .container {
    flex-direction: column; /* Stacks items vertically */
  }
}

.bubble-title {
  font-weight: bold;
  flex: 1 1 auto; /* Allows items to grow and shrink */
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

.emergency {
  background-color: red;
  text-decoration: underline;
  animation: vertical-shaking 4s infinite;
}
.green {
  background-color: green;
  text-decoration: green;
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
  top: 390px; /* Adjust this based on your header height */
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