<template>
  <section>
    <div class="fixed-header">
      <company-header />
      <manager-greeting />
      <div id="backImage">
        <div class="overlay"></div>
        <div class="content">
          <manager-navigation />

          <div id="search-shifts">
            <div class="filter">
              <div>
                <input
                  type="text"
                  id="assignedNameFilter"
                  placeholder="Name"
                  v-model="filter.assignedName"
                />
              </div>
              <div>
                <input
                  type="date"
                  id="startDateTime"
                  v-model="filter.startDateTime"
                />
              </div>
              <div>
                <input
                  type="text"
                  id="duration"
                  v-model="filter.duration"
                  placeholder="Duration"
                />
              </div>
              <div>
                <select id="myList" v-model="filter.status">
                  <option value="--None--">--None--</option>
                  <option value="uncovered request">Uncovered Request</option>
                  <option value="covered">Covered</option>
                  <option value="uncovered">Uncovered</option>
                  <option value="assigned">Assigned</option>
                </select>
              </div>

              <div>
                <select id="myList" v-model="filter.emergency">
                  <option value="--None--">--None--</option>
                  <option value="true">true</option>
                  <option value="false">false</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="scrollable-container">
        <div class="scrollable-content">
          <div class="content">
            <div id="data" v-for="shift in filteredList" :key="shift.shiftId">
              <template v-if="coverRequestsCount[shift.shiftId] > 0">
                <!-- <router-link -->
                  <!-- :to="{
                    name: 'pendingCoverRequests',
                    params: { shiftId: shift.shiftId },
                  }" -->
                <!-- > -->
                  <div
                    class="bubble"
                    :class="{
                      emergency: shift.emergency && shift.status === 3,
                      green: shift.status === 4 || shift.status === 1,
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
                      <p class="bubble-title">
                        Emergency: {{ shift.emergency }}
                      </p>
                    </div>
                    <div id="shiftObjects6">
                      <button v-if="shift.status === 3" @click="newPage(shift.shiftId)" class="button-title">
                        Cover Requests:
                        {{ coverRequestsCount[shift.shiftId] }}
                      </button>
                    </div>
                  </div>
                <!-- </router-link> -->
              </template>
              <template v-else>
                <div
                  class="bubble"
                  :class="{
                    emergency: shift.emergency && shift.status === 3,
                    green: shift.status === 4 || shift.status === 1,
                  }"
                >
                  <div class="together">
                    <div id="shiftObjects7">
                      <p class="bubble-title">Name: {{ shift.assignedName }}</p>
                    </div>
                    <div id="shiftObjects8">
                      <p class="bubble-title">
                        Start Time: {{ shift.startDateTime }}
                      </p>
                    </div>
                    <div id="shiftObjects9">
                      <p class="bubble-title">
                        Duration: {{ shift.duration }} <span>hours</span>
                      </p>
                    </div>
                    <div id="shiftObjects10">
                      <p class="bubble-title">
                        Status: {{ convertStatus(shift.status) }}
                      </p>
                    </div>
                    <div id="shiftObjects11">
                      <p class="bubble-title">
                        Emergency: {{ shift.emergency }}
                      </p>
                    </div>
                  </div>
                </div>
              </template>
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
      coverRequestsCount: {},
      listOfShifts: [],
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

    newPage(shiftId) {
      this.$router.push(`/shift/${shiftId}/cover`)
    },
    async getAllShifts() {
      try {
        const response = await ShiftService.getShifts();
        this.listOfShifts = response.data;

        // Fetch cover requests for each shift
        for (let shift of this.listOfShifts) {
          await this.getCoverRequests(shift.shiftId);
        }
      } catch (error) {
        console.error("Error fetching shifts:", error);
      }
    },
    async getCoverRequests(shiftID) {
      ManagerService.getManagerViewCoverRequests(shiftID).then((response) => {
        this.coverRequestsCount[shiftID] = response.data.length;
      });
    },
    async getFullName() {
      try {
        const response = await ShiftService.getUserFullName();
        this.name = response.data;
        this.$store.commit("ADD_NAME", this.name);
      } catch (error) {
        console.error("Error fetching user name:", error);
      }
    },
    convertStatus(status) {
      const statusMap = {
        1: "assigned",
        2: "uncovered request",
        3: "uncovered",
        4: "covered",
      };
      return statusMap[status] || "";
    },
    convertStatusToNumber(status) {
      const statusMap = {
        assigned: 1,
        "uncovered request": 2,
        uncovered: 3,
        covered: 4,
      };
      return statusMap[status.toLowerCase()] || 0;
    },
    convertStringToBoolean(emergency) {
      return emergency === "true";
    },
  },
  async created() {
    await this.getAllShifts();
    await this.getFullName();
  },

  computed: {
    filteredList() {
      return this.listOfShifts.filter((shift) => {
        return (
          (!this.filter.assignedName ||
            shift.assignedName
              .toLowerCase()
              .includes(this.filter.assignedName.toLowerCase())) &&
          (!this.filter.startDateTime ||
            shift.startDateTime.includes(this.filter.startDateTime)) &&
          (!this.filter.duration ||
            shift.duration === parseInt(this.filter.duration)) &&
          (this.filter.status === "--None--" ||
            this.convertStatus(shift.status) === this.filter.status) &&
          (this.filter.emergency === "--None--" ||
            shift.emergency ===
              this.convertStringToBoolean(this.filter.emergency))
        );
      });
    },
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
}

.bubble p {
  margin: 0;
  padding: 0;
  margin-right: 20px;
}

#shiftObjects6 {
  width: 150px;
  height: 5px;
  max-width: 100%;
  width: auto;
}

#shiftObjects6:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  background-color: lightgray;
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
  margin: 0 auto;
  width: 80%;
  max-width: fit-content;
  position: relative;
  z-index: 2;
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
  z-index: 1;
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
  top: 20em;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 1;
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