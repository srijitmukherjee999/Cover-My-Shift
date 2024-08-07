<template>
        
    <div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: 40</h1>
 
  
    
    </div>
  <nav class="navigation">
    <ul>
        <li><router-link v-bind:to="{name: 'employee'}">MY HOME</router-link></li>
    
        <li><router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link></li>
    
        <li><router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link></li>
    </ul>
  </nav>

  <div class="filter">
    <input type="text" id="assignedNameFilter" placeholder="Name" v-model="filter.assignedName" />&nbsp;&nbsp;

    <input type="date" id="startDateTime" v-model="filter.startDateTime"  />&nbsp;&nbsp;
    <input type="text" id="duration" v-model="filter.duration" placeholder="Duration">&nbsp;&nbsp;
    <select id = "myList" v-model="filter.status" >
    <option  id="">--None--</option>
    <option value="accepted">Accepted</option>
    <option value="covered">Covered</option>
    <option value="uncovered">Uncovered</option>
    <option value="assigned">Assigned</option>
    </select>&nbsp;&nbsp;

    
    <select id = "myList" v-model="filter.emergency" >Emergency
    <option  id="emergency">--None--</option>
    <option value="true">true</option>
    <option value="false">false</option>
    </select>
  </div>

  <div id="data" v-for="shift in filteredList" :key="shift.shiftId">
    <router-link :to="{ name: 'shiftdetails', params: { id: shift.shiftId }} ">
      <div class="bubble" :class="{emergency : shift.emergency}">
        <p class="bubble-title">Name</p>
        <p>{{ shift.assignedName }}</p>
      
      
        <p class="bubble-title">Start Time</p>
        <p>{{ shift.startDateTime }}</p>
      
      
        <p class="bubble-title">Duration</p>
        <p>{{ shift.duration }} <span>hours</span></p>
      
      
        <p class="bubble-title">Status</p>
        <p>{{ convertStatus(shift.status) }}</p>
      
      
        <p class="bubble-title">Emergency</p>
        <p>{{ shift.emergency }}</p>
      </div>
    </router-link>
  </div>  
</template>

<script>
import ShiftService from '../services/ShiftService';

export default {

    data(){
        
        return{
            listOfShifts : [
                {
                    assignedName : '',
                    shiftId: 0,
                    assigned: 0,
                    startDateTime: '',
                    duration: 0,
                    status: 0,
                    emergency: false,
                    coverer: 0,
                    covererName: '',
                    description: ''
                    
                }
            ],
            name: '',
            filter: {
                    assignedName : '',          
                    startDateTime: '',
                    duration: '',
                    status: '--None--',
                    emergency: '--None--',
                    
            }
        }
    }
    ,

    methods:{
        getAllShifts(){
            ShiftService.getShifts().then(response => {
               this.listOfShifts = response.data;
              
            })
        },
        getFullName(){

        ShiftService.getUserFullName().then( response => {

                 this.name = response.data;

                this.$store.commit("ADD_NAME", this.name);
        })

        },
        
        
        convertStatus(status){
            
            if(status == 1)
                return "assigned"
            if(status == 2)
            return "accepted"
                if(status === 3)
                return "uncovered"
            if(status == 4)
            return "covered"

        },
        
        convertStatusToNumber(status){
            if(status.toLowerCase().includes("assigned"))
            return 1
            if(status.includes("accepted"))
            return 2
        if(status.includes("uncovered"))
        return 3
    if(status.includes("covered"))
    return 4
    
        }

    
   


    },
    created(){
        
        this.getAllShifts();
        this.getFullName();
        
        
    },
    computed: {
        shiftDetails(){
            const shiftId = this.$route.params.id;
            return this.listOfShifts.find( e => e.shiftId === shiftId);
        },
        filteredList() {
      let filteredUsers = this.listOfShifts;
      if (this.filter.name != "") {
        filteredUsers = filteredUsers.filter((shift) =>
          shift.assignedName.toLowerCase().includes(this.filter.assignedName.toLowerCase())      
        );
      }
      if (this.filter.startDateTime != "") {
        filteredUsers = filteredUsers.filter((shift) =>
          shift.startDateTime.includes(this.filter.startDateTime)
        );
      }
      if (this.filter.duration != 0) {
        filteredUsers = filteredUsers.filter(( shift) =>
          shift.duration == (this.filter.duration)
        );
      }
      if ((this.filter.status != "--None--")) {
        filteredUsers = filteredUsers.filter((shift) =>
            this.convertStatus(shift.status) == (this.filter.status.toLowerCase())
        );
      }
      if(this.filter.emergency != "--None--"){
        filteredUsers = filteredUsers.filter(shift => {
          shift.emergency == true;
        })
      }

      
      return filteredUsers;
    }
        
    }

}
</script>

<style scoped>

.yes{
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
}

.bubble:hover {
  transform: scale(1.05); 
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.bubble-title {
  font-weight: bold; 
  margin-bottom: 10px; 
}

.bubble p {
    margin: 0;
    padding: 0;
    margin-right: 20px;
}

.emergency{
    background-color: red;
    text-decoration: underline;
}

.filter{
    display: flex;
    justify-content: center;
}
</style>