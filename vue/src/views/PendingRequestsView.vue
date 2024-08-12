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
                    <p>{{ shift.startDateTime }}</p>
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
                  </div>
                  <div>
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
    getShiftPendingRequests() {
      ShiftService.getShiftsByUncoveredRequest(2).then((response) => {
        this.listOfPendingRequests = response.data;
      });
    },
    acceptShift(shiftId, employeeId) {
      const approvalObject = this.createApprovalObject(
        shiftId,
        employeeId,
        true
      );
      ManagerService.acceptRejectRequest(approvalObject).then((response) => {
        if (response.status === 200) {
          alert(`Accepted shift with ID: ${shiftId}`);
          this.getMyShiftPendingRequests();
        }
      });
    },
    rejectShift(shiftId, employeeId) {
      const rejectionObject = this.createApprovalObject(
        shiftId,
        employeeId,
        false
      );
      ManagerService.acceptRejectRequest(rejectionObject).then((response) => {
        if (response.status === 200) {
          alert(`Rejected shift with ID: ${shiftId}`);
          this.getMyShiftPendingRequests();
        }
      });
    },
    createApprovalObject(shiftId, employeeId, isApproved) {
      return {
        shiftId: shiftId,
        employeeId: employeeId,
        approved: isApproved,
        message: "yes",
      };
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
  },
  created() {
    this.getFullName();
    this.getShiftPendingRequests();
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

@media (max-width: 600px) {
  .container {
    flex-direction: column;
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
</style>
