<template>
  <div class="fixed-header">
    <company-header />
    <!-- v-if="userRole === 'ROLE_MANAGER'" -->
    <manager-greeting />

  <div id="backImage">
    <div class="overlay"></div>
    <div class="content">
   <manager-navigation/>
  

    <!-- <div v-if="isManager">
      <nav class="navigation">
        <ul>
          <li><router-link v-bind:to="{ name: 'manager' }">MY HOME</router-link></li>
          <li><router-link v-bind:to="{ name: 'pendingrequests' }">PENDING REQUESTS</router-link></li>
          <li><router-link v-bind:to="{ name: 'pickupshift' }">FIRE EMPLOYEE</router-link></li>
        </ul>
      </nav>
    </div> -->

  

    <div id="shift-inputs">
      <div class="filter">
        <div><input type="date" v-model="shiftInputs.startDate" placeholder="Start Date" /></div>
        <div><input type="date" v-model="shiftInputs.endDate" placeholder="End Date (optional)" /></div>
        <div><input type="time" v-model="shiftInputs.startTime" placeholder="Start Time" /></div>
        <div><input type="number" v-model="shiftInputs.duration" placeholder="Duration (hours)" /></div>
        <div><button class="submit-button" @click="submitShifts">Submit Shifts</button></div>
      </div>
    </div>
    </div>

    <div class="scrollable-container">
      <div class="scrollable-content">
        <div class="content">
        <div id="data" v-for="user in listOfUsers" :key="user.id">
          <div class="together">
            <div class="bubble">
              <div class="bubble-title"><p>{{ user.fullName }}</p></div>
              <div>
                <button :class="['add-shift-button', isSelected(user.id) ? 'selected-button' : 'add-button']" @click="toggleSelection(user.id)">
                  {{ isSelected(user.id) ? 'Selected' : 'Add Shift' }}
                </button>
              </div>
            </div>
          </div>
        </div>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script>
  import ShiftService from "../services/ShiftService.js";
  import AuthService from "../services/AuthService";
  import CompanyHeader from '../components/CompanyHeader.vue';
  import ManagerNavigation from "../components/ManagerNavigation.vue";
import ManagerGreeting from '../components/ManagerGreeting.vue';
  
  export default {
    components: { CompanyHeader,
                  ManagerNavigation,
                  ManagerGreeting,  
    },
  
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
        userRole:'',
        isManager:false,
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

      

  //     computed: {
  //       userRole() {
  //         return this.$store.state.user.authorities[0].name; // Adapt this based on your state management
  //   }
  // }

    },
  
    created() {
      
      // this.getAllUsers();
      // this.getFullName();
      ///////
      this.userRole =this.$store.state.user.authorities[0].name;
      this.isManager = this.userRole ==="ROLE_MANAGER";
      if(!this.isManager){
        this.$router.push('/login');  // this to redirect to login/register page
      }else{

        this.getAllUsers();
        this.getFullName();
      }
    },
  }
</script>
  
  <style>

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
  
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  #shift-inputs {
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

input[type="time"] {
  width: 200px;         
  height: 50px;         
  padding: 10px;        
  font-size: 18px;      
  border: 2px solid #000; 
  border-radius: 5px;
  text-align: center;  
}

input[type="number"] {
  width: 200px;         
  height: 50px;         
  padding: 10px;        
  font-size: 18px;      
  border: 2px solid #000; 
  border-radius: 5px;
  text-align: center;  
}

.submit-button {
  width: 200px;         
  height: 50px;         
  padding: 10px;        
  font-size: 18px;      
  border: 2px solid #000; 
  border-radius: 5px;
  text-align: center;  
}

body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  background: transparent;
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background: white; /* Ensure visibility if needed */
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

#shift-inputs {
  margin: 0 auto;
  width: 80%;
  max-width: fit-content;
  padding: 20px;
  position: relative;
  z-index: 2; /* Ensure it is above the overlay but below scrolling content */
}

.scrollable-container {
  position: fixed;
  top: 300px; /* Adjust this based on your header height */
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
  