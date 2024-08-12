<template>
  <div class="fixed-header">
    <company-header/>
    <ManagerGreeting/>
    <ManagerNavigation/>
    <div>
      <h1>Pending Cover Requests for Shift ID: {{ shiftId }}</h1>
      <ul>
        <li v-for="request in coverRequests" :key="request.id">
          <div class="bubble">
            <div class="bubble-info">
              <p class="bubble-title">Request by: {{ request.fullName }}</p>
            </div>
            <div class="bubble-actions">
              <button class="accept-button" @click="openPopup('accept', shiftId, request.id)">Accept</button>
              <button class="reject-button" @click="openPopup('reject', shiftId, request.id)">Reject</button>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <!-- Popup for message input -->
    <div v-if="showPopup" class="popup-overlay">
      <div class="popup-content">
        <h2>{{ popupAction === 'accept' ? 'Accept' : 'Reject' }} Cover Request</h2>
        <textarea v-model="message" placeholder="Enter your message here..."></textarea>
        <div class="popup-buttons">
          <button @click="submitAction">Submit</button>
          <button @click="closePopup">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CompanyHeader from '../components/CompanyHeader.vue';
import ManagerGreeting from '../components/ManagerGreeting.vue';
import ManagerNavigation from '../components/ManagerNavigation.vue';
import ManagerService from '../services/ManagerService';

export default {
  data() {
    return {
      shiftId: this.$route.params.shiftId,
      coverRequests: [],
      message: '',
      showPopup: false,
      popupAction: '',
      selectedShiftId: null,
      selectedEmployeeId: null
    };
  },
  methods: {
    getCoverRequests() {
      ManagerService.getCoverRequests(this.shiftId,1).then(response => {
        this.coverRequests = response.data;
      })
      .catch(error => {
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
      this.message = ''; 
    },
    submitAction() {
      const approvalObject = this.createApprovalObject(this.selectedShiftId, this.selectedEmployeeId, this.popupAction === 'accept');
      ManagerService.acceptRejectRequest(approvalObject).then(response => {
        if (response.status === 200) {
          alert(`${this.popupAction === 'accept' ? 'Accepted' : 'Rejected'} shift with ID: ${this.selectedShiftId}`);
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
        message: this.message
      };
    }
  },
  created() {
    this.getCoverRequests();
  },
  components: { CompanyHeader, ManagerGreeting, ManagerNavigation }
};
</script>

<style scoped>
.bubble {
  background-color: #4a90e2;
  color: white;
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  text-align: left;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
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
</style>

