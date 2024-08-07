<template>
    <div class="yes">
      <h1>Hello Manager {{ name }}</h1>
    </div>
    <div>
      <nav class="navigation">
        <ul>
          <li><router-link v-bind:to="{ name: 'employee' }">MY HOME</router-link></li>
          <li><router-link v-bind:to="{ name: 'timeoff' }">PENDING REQUESTS</router-link></li>
          <li><router-link v-bind:to="{ name: 'pickupshift' }">FIRE EMPLOYEE</router-link></li>
        </ul>
      </nav>
    </div>
  
    <div id="data">
      <div v-for="user in listOfUsers" :key="user.id" class="bubble">
        <div class="bubble-title">{{ user.fullName }}</div>
        <button
          :class="['add-shift-button', user.showShiftForm ? 'cancel-button' : 'add-button']"
          @click="toggleShiftForm(user.id)"
        >
          {{ user.showShiftForm ? 'Cancel' : 'Add Shift' }}
        </button>
        <div v-if="user.showShiftForm" class="shift-form">
          <input
            type="datetime-local"
            v-model="newShift.startDateTime"
            placeholder="Start Date and Time"
          />
          <input
            type="number"
            v-model="newShift.duration"
            placeholder="Duration (hours)"
          />
          <input
            type="text"
            v-model="newShift.description"
            placeholder="Description"
          />
          <button @click="addShift(user.id)">Submit</button>
        </div>
      </div>
    </div>
  </template>
  
  
  <script>
  import ShiftService from "../services/ShiftService";
  import AuthService from "../services/AuthService";
  
  export default {
    data() {
      return {
        listOfShifts: [
          {
            name: "",
            shiftId: 0,
            assigned: 0,
            startDateTime: "",
            duration: 0,
            status: 0,
            emergency: false,
            coverer: 0,
            description: "",
          },
        ],
        listOfUsers: [
          {
            id: 0,
            username: "",
            fullName: "",
            authorities: [
              {
                name: "",
              },
            ],
            showShiftForm: false, // Track form visibility per user
          },
        ],
        newShift: {
          shiftId: 0,
          assignedId: null,
          startDateTime: "",
          duration: 0,
          status: 0,
          emergency: false,
          covererId: null,
          description: "",
        },
      };
    },
  
    methods: {
      getAllShifts() {
        ShiftService.getShifts().then((response) => {
          this.listOfShifts = response.data;
          this.getNameByShift();
        });
      },
  
      getUser(id) {
        ShiftService.getUserByUserId(id).then((response) => {
          this.$store.state.user = response.data;
        });
      },
  
      getNameByShift() {
        this.listOfShifts.forEach((e) => {
          this.name = this.getUser(e.id).name;
        });
      },
  
      getAllUsers() {
        AuthService.getUsers().then((response) => {
          this.listOfUsers = response.data.map((user) => ({
            ...user,
            showShiftForm: false,
          }));
        });
      },
  
      toggleShiftForm(userId) {
        this.listOfUsers = this.listOfUsers.map((user) =>
          user.id === userId ? { ...user, showShiftForm: !user.showShiftForm } : user
        );
      },
  
      addShift(userId) {
        this.newShift.assignedId = userId;
        ShiftService.createShift(this.newShift).then((response) => {
          if (response.status === 201) {
            alert("Shift has been added to employee");
            this.toggleShiftForm(userId); // Close the form after adding the shift
            this.newShift = {
              shiftId: 0,
              assignedId: null,
              startDateTime: "",
              duration: 0,
              status: 0,
              emergency: false,
              covererId: null,
              description: "",
            }; // Reset the form after submission
          }
        });
      },
    },
  
    created() {
      this.getAllShifts();
      this.getAllUsers();
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
  