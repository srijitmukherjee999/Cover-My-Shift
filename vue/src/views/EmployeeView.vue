<template>
    <body>
      <company-header/>
      <employee-greeting/>
      <employee-navigation/>
    
  <div id="search-shifts">
  <div class="filter">
    <input type="text" id="assignedNameFilter" placeholder="Name" v-model="filter.assignedName" />
    <input type="date" id="startDateTime" v-model="filter.startDateTime"  />
    <input type="text" id="duration" v-model="filter.duration" placeholder="Duration">
    <select id = "myList" v-model="filter.status" >
    <option  id="status">--None--</option>
    <option value="uncovered request">Uncovered Request</option>
    <option value="covered">Covered</option>
    <option value="uncovered">Uncovered</option>
    <option value="assigned">Assigned</option>
    </select>

    
    <select id = "myList" v-model="filter.emergency" >Emergency
    <option  id="emergency">--None--</option>
    <option value="true">true</option>
    <option value="false">false</option>
    </select>
  </div>
  </div>

  <div id="data" v-for="shift in filteredList" :key="shift.shiftId">
    <router-link :to="{ name: 'shiftdetails', params: { id: shift.shiftId }} ">
      <div class="bubble" :class="{emergency : shift.emergency && shift.status == 3, green: shift.status == 4 || shift.status == 1}" >
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

</body>
</template>

<script>
import CompanyHeader from '../components/CompanyHeader.vue';
import EmployeeGreeting from '../components/EmployeeGreeting.vue';
import EmployeeNavigation from '../components/EmployeeNavigation.vue';
import ShiftService from '../services/ShiftService';


export default {
  components: { CompanyHeader,
                EmployeeGreeting,
                EmployeeNavigation,
  },
  

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
                    
            },
     
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
            return "uncovered request"
                if(status === 3)
                return "uncovered"
            if(status == 4)
            return "covered"

        },
        
        convertStatusToNumber(status){
          if(status.toLowerCase().includes("assigned"))
            return 1
          if(status.includes("uncovered request"))
            return 2
          if(status.includes("uncovered"))
            return 3
          if(status.includes("covered"))
            return 4
        },
        convertStringToBoolean(emergency){
          if(this.filter.emergency === 'true')
            return true
          if(this.filter.emergency === 'false')
            return false
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
        if (this.filter.assignedName != "") {
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

        if( this.filter.emergency != '--None--'){
          filteredUsers = filteredUsers.filter((shift) => 
            shift.emergency == this.convertStringToBoolean(this.filter.emergency)
          );
        }

        return filteredUsers;
      },


      }

}
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
   animation: vertical-shaking 4s infinite;
}
.green{
  background-color: green;
  text-decoration: green;
}

#search-shifts {
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

#myList {
    width: 200px;         
  height: 50px;         
  padding: 10px;        
  font-size: 18px;      
  border: 2px solid #000; 
  border-radius: 5px;
  text-align: center;  
}
.myFilter{
  
  justify-content: center;
  align-content: center;
}

@keyframes vertical-shaking {
  0% { transform: translateY(0) }
  25% { transform: translateY(5px) }
  50% { transform: translateY(-5px) }
  75% { transform: translateY(5px) }
  100% { transform: translateY(0) }
}
h1{
  font-style: italic;
  font-weight: bold;
   animation: fadeIn 4s;
  
}
</style>

