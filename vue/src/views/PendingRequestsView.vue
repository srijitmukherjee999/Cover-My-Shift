<template>
  <company-header/>

  <div class="yes">
    <h1>Hello Manager {{ name }}</h1>
  </div>

  <div>
    <nav class="navigation">
      <ul>
        <li><router-link v-bind:to="{ name: 'manager' }">MY HOME</router-link></li>
        <li><router-link v-bind:to="{ name: 'pendingrequests' }">PENDING REQUESTS</router-link></li>
        <li><router-link v-bind:to="{ name: 'pickupshift' }">FIRE EMPLOYEE</router-link></li>
      </ul>
    </nav>
  </div>

  <div id="data">
    <div v-for="shift in listOfPendingRequests" v-bind:key="shift.shiftId" class="bubble">
      <div class="bubble-title">{{ shift.assignedName }}</div>
      <div class="bubble-info">
        <p>{{ shift.startDateTime }}</p>
        <p>{{ shift.duration }} hours</p>
        <p>{{ shift.emergency ? 'Emergency' : 'Regular' }}</p>
      </div>
      <div class="bubble-actions">
        <button class="accept-button" @click="acceptShift(shift.shiftId, shift.employeeId)">Accept</button>
        <button class="reject-button" @click="rejectShift(shift.shiftId, shift.employeeId)">Reject</button>
      </div>
    </div>
  </div>
</template>

  
<script>
import CompanyHeader from '../components/CompanyHeader.vue';
import ShiftService from '../services/ShiftService';
import ManagerService from '../services/ManagerService'

export default {
  data(){
    return {
      name: '',
      listOfPendingRequests: [],
     
    }
  },
  components: { CompanyHeader },
  methods: {
    getFullName(){
      ShiftService.getUserFullName().then(response => {
        this.name = response.data;
        this.$store.commit("ADD_NAME", this.name);
      });
    },
    getMyShiftPendingRequests(){
      ShiftService.getMyShiftsByUncoveredRequest(2).then(response => {
        this.listOfPendingRequests = response.data;
      });
    },
    acceptShift(shiftId, employeeId) {
    const approvalObject = this.createApprovalObject(shiftId, employeeId, true);
    ManagerService.acceptRejectRequest(approvalObject).then(response => {
      if (response.status === 200) {
        alert(`Accepted shift with ID: ${shiftId}`);
        this.getMyShiftPendingRequests();
      }
    });
  },
  rejectShift(shiftId, employeeId) {
    const rejectionObject = this.createApprovalObject(shiftId, employeeId, false);
    ManagerService.acceptRejectRequest(rejectionObject).then(response => {
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
      approved: isApproved
    };
    },
  },
  created(){
    this.getFullName();
    this.getMyShiftPendingRequests();
  }
}
</script>

  
<style scoped>
.yes {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
  padding: 10px;
  background-color: white;
}

.navigation {
  padding: 10px;
  margin: 20px;
  border-radius: 5px;
}

.navigation a {
  text-decoration: none;
  color: #000000;
}

.navigation ul {
  list-style: none;
  padding: 0;
  margin: center;
  text-align: center;
}

.navigation li {
  display: inline;
  margin-right: 15px;
  font-size: larger;
  background-color: white;
  color: black;
  border-radius: 50px;
  padding: 20px;
  box-shadow: 0 4px 8px;
  width: 100%;
  transition: transform 0.3s, box-shadow 0.3s;
  font: bold;
}

.navigation li:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  background-color: lightgray;
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
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  text-align: left;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
  position: relative;
}

.bubble-title {
  font-weight: bold;
  margin-bottom: 10px;
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
  border-radius: 20px;
  padding: 10px 20px;
  cursor: pointer;
  color: white;
  font-weight: bold;
}

.accept-button:hover {
  background-color: #4cae4c;
}

.reject-button {
  background-color: #d9534f;
  border: none;
  border-radius: 20px;
  padding: 10px 20px;
  cursor: pointer;
  color: white;
  font-weight: bold;
}

.reject-button:hover {
  background-color: #c9302c;
}
</style>
