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
  
    <div class="shift-inputs">
      <input
        type="date"
        v-model="shiftInputs.startDate"
        placeholder="Start Date"
      />
      <input
        type="date"
        v-model="shiftInputs.endDate"
        placeholder="End Date (optional)"
      />
      <input
        type="time"
        v-model="shiftInputs.startTime"
        placeholder="Start Time"
      />
      <input
        type="number"
        v-model="shiftInputs.duration"
        placeholder="Duration (hours)"
      />
      <button class="submit-button" @click="submitShifts">
      Submit Shifts
    </button>
    </div>
  
    <div id="data">
      <div v-for="user in listOfUsers" :key="user.id" class="bubble">
        <div class="bubble-title">{{ user.fullName }}</div>
        <button
          :class="['add-shift-button', isSelected(user.id) ? 'selected-button' : 'add-button']"
          @click="toggleSelection(user.id)"
        >
          {{ isSelected(user.id) ? 'Selected' : 'Add Shift' }}
        </button>
      </div>
    </div>
    
  </template>
  
  <script>
  import ShiftService from "../services/ShiftService.js";
  import AuthService from "../services/AuthService";
  import CompanyHeader from '../components/CompanyHeader.vue';
  
  export default {
    components: { CompanyHeader },
  
    data() {
      return {
        name: '',
        shiftInputs: {
          startDate: "",
          endDate: "",
          startTime: "",
          duration: 0,
        },
        listOfUsers: [],
        selectedUsers: [],
      };
    },
  
    methods: {
      getAllUsers() {
        AuthService.getUsers().then((response) => {
          this.listOfUsers = response.data.map((user) => ({
            ...user,
            showShiftForm: false,
          }));
        });
      },
  
      toggleSelection(userId) {
        const index = this.selectedUsers.indexOf(userId);
        if (index !== -1) {
          this.selectedUsers.splice(index, 1);
        } else {
          this.selectedUsers.push(userId);
        }
      },
      getFullName(){

ShiftService.getUserFullName().then( response => {

         this.name = response.data;

        this.$store.commit("ADD_NAME", this.name);
})

},
  
      isSelected(userId) {
        return this.selectedUsers.includes(userId);
      },
  
      submitShifts() {
        this.selectedUsers.forEach((userId) => {
          let startDate = new Date(this.shiftInputs.startDate);
          const endDate = this.shiftInputs.endDate ? new Date(this.shiftInputs.endDate) : startDate;
  
          while (startDate <= endDate) {
            const startDateTime = new Date(startDate);
            startDateTime.setHours(...this.shiftInputs.startTime.split(":"));
  
            const newShift = {
              assignedId: userId,
              startDateTime: startDateTime.toISOString(),
              duration: this.shiftInputs.duration,
              status: 1,
              emergency: false,
              covererId: userId,
              description: `Shift for ${startDateTime.toLocaleDateString()}`
            };
  
            ShiftService.createShift(newShift).then((response) => {
              if (response.status === 201) {
                alert(`Shift has been added to employee ${userId}`);
              }
            });
  
            startDate.setDate(startDate.getDate() + 1);
          }
        });
  
        this.selectedUsers = []; 
      },
    },
  
    created() {
      this.getAllUsers();
      this.getFullName();
    
    },
  };
</script>

  
  <style>
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
    flex-wrap: wrap;
    align-items: center;
    text-align: left;
    box-sizing: border-box;
    transition: transform 0.3s, box-shadow 0.3s;
    position: relative;
  }
  
  .bubble-title {
    font-weight: bold;
    margin-bottom: 10px;
    flex-grow: 1;
  }
  
  .add-shift-button {
    border: none;
    border-radius: 20px;
    padding: 10px 20px;
    cursor: pointer;
    transition: background-color 0.3s, box-shadow 0.3s;
    margin-left: auto;
  }
  
  .add-button {
    background-color: #5cb85c; /* Green */
  }
  
  .add-button:hover {
    background-color: #4cae4c; /* Darker green */
  }
  
  .cancel-button {
    background-color: #d9534f; /* Red */
  }
  
  .cancel-button:hover {
    background-color: #c9302c; /* Darker red */
  }
  
  .shift-form {
    display: flex;
    flex-direction: column;
    gap: 10px;
    background-color: #fff;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    animation: fadeIn 0.3s;
    z-index: 1;
  }
  
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
  </style>
  