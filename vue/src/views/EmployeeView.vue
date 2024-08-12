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
  },

  created() {
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

@media (max-width: 600px) {
  .container {
    flex-direction: column;
  }
  .scrollable-container {
  position: fixed;
  top: 40em; /* Adjust this based on your header height */
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 1; /* Less than header */
}
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
  margin: auto;
  width: 80%;
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
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background: rgba(0, 0, 0, 0.8);
  z-index: 1;
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
  top: 29em; /* Adjust this based on your header height */
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

