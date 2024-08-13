<template>
  <section>
    <div class="fixed-header">
      <company-header />
      <employee-greeting />
      <div id="backImage">
        <div class="overlay"></div>
        <div class="content">
          <employee-navigation />
          <request-off-form />

          <h2>Your Pending Requests:</h2>
        </div>

        <div class="scrollable-container">
          <div class="scrollable-content">
            <div class="content">
              <div
                id="data"
                v-for="shift in listOfPendingRequests"
                v-bind:key="shift.shiftId"
              >
                <div class="together">
                  <div
                    class="bubble"
                    :class="{
                      emergency: shift.emergency && shift.status == 3,
                      green: shift.status == 4 || shift.status == 1,
                    }"
                  >
                    <div id="shiftObjects">
                      <p class="bubble-title">Name: {{ shift.assignedName }}</p>
                    </div>

                    <div id="shiftObjects">
                      <p class="bubble-title">
                        Start Time: {{ shift.startDateTime }}
                      </p>
                    </div>

                    <div id="shiftObjects">
                      <p class="bubble-title">
                        Duration: {{ shift.duration }} <span>hours</span>
                      </p>
                    </div>

                    <div id="shiftObjects">
                      <p class="bubble-title">
                        Emergency: {{ shift.emergency }}
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
import RequestOffForm from "../components/RequestOffForm.vue";
import ShiftService from "../services/ShiftService";
export default {
  data() {
    return {
      name: "",

      listOfPendingRequests: [
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
    };
  },
  components: {
    RequestOffForm,
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

    getMyShiftPendingRequests() {
      ShiftService.getMyShiftsByUncoveredRequest(true, 2).then((response) => {
        this.listOfPendingRequests = response.data;
      });
    },
    convertStatus(status) {
      if (status == 1) return "assigned";
      if (status == 2) return "uncovered request";
      if (status === 3) return "uncovered";
      if (status == 4) return "covered";
    },
    
  },
  created() {
    this.getFullName();
    this.getMyShiftPendingRequests();
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
  top: 25em; /* Adjust this based on your header height */
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
  z-index: 1;
}
</style>