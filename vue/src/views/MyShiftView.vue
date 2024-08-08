<template>

<div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: 40</h1>
 </div>

  <div>
    <nav class="navigation">
    <ul>
        <li><router-link v-bind:to="{name: 'employee'}">MY HOME</router-link></li>
    
        <li><router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link></li>
    
        <li><router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link></li>
        
        <li><router-link v-bind:to="{name: 'myshift'}">MY SHIFT</router-link></li>
    </ul>
  </nav>
  </div>
  
  <div class="MyFilter">
    

    <input type="date" id="startDateTime" v-model="myFilter.startDateTime"  />&nbsp;&nbsp;
    <input type="text" id="duration" v-model="myFilter.duration" placeholder="Duration">&nbsp;&nbsp;
    <select id = "myList" v-model="myFilter.status" >
    <option  id="">--None--</option>
    <option value="uncovered request">Uncovered request</option>
    <option value="covered">Covered</option>
    <option value="uncovered">Uncovered</option>
    <option value="assigned">Assigned</option>
    </select>&nbsp;&nbsp;

    <select id = "myList" v-model="myFilter.emergency" >Emergency
    <option  id="emergency">--None--</option>
    <option value="true">true</option>
    <option value="false">false</option>
    </select>
  </div>

  <div id="myData" v-for="shift in filteredMyList" :key="shift.shiftId">
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
            name: '',
            listOfMyShifts: [
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
            myFilter: {
                    assignedName : '',          
                    startDateTime: '',
                    duration: '',
                    status: '--None--',
                    emergency: '--None--',
                    
            },
        }
    }, 
    methods:{

        getMyShifts(){

            ShiftService.getMyShifts(true).then(response => {

             this.listOfMyShifts = response.data;

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
          getFullName(){

            ShiftService.getUserFullName().then( response => {

          this.name = response.data;

          this.$store.commit("ADD_NAME", this.name);
          })

            },
            convertStringToBoolean(emergency){
              if(emergency === 'true')
              return true
              if(emergency === 'false')
              return false
          }



    }, 
    computed: {

          filteredMyList() {
        let filteredUsers = this.listOfMyShifts;
        
        if (this.myFilter.startDateTime != "") {
          filteredUsers = filteredUsers.filter((shift) =>
            shift.startDateTime.includes(this.myFilter.startDateTime)
          );
        }
        if (this.myFilter.duration != 0) {
          filteredUsers = filteredUsers.filter(( shift) =>
            shift.duration == (this.myFilter.duration)
          );
        }
        if ((this.myFilter.status != "--None--")) {
          filteredUsers = filteredUsers.filter((shift) =>
              this.convertStatus(shift.status) == (this.myFilter.status.toLowerCase())
          );
        }
        if( this.myFilter.emergency != '--None--'){
          filteredUsers = filteredUsers.filter(shift  => 
            shift.emergency === this.convertStringToBoolean(this.myFilter.emergency)
          )
        }
        
        
        return filteredUsers;
      }
    }, 
    created(){

      this.getMyShifts();
      this.getFullName();
    }

    



}
</script>


<style scoped>

.MyFilter{

  display: flex;
  justify-content: center;
}

#myData{

  display: flex;
  justify-content: center;
  padding-top: 3%;

}

h1{
  font-style: italic;
  font-weight: bold;
  animation: fadeIn 2s;
}

</style>