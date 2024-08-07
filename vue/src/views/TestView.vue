<template>
  <div class="MyFilter">
    <input type="text" id="assignedNameFilter" placeholder="Name" v-model="myFilter.assignedName" />&nbsp;&nbsp;

    <input type="date" id="startDateTime" v-model="myFilter.startDateTime"  />&nbsp;&nbsp;
    <input type="text" id="duration" v-model="myFilter.duration" placeholder="Duration">&nbsp;&nbsp;
    <select id = "myList" v-model="myFilter.status" >
    <option  id="">--None--</option>
    <option value="accepted">Accepted</option>
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
}



    }, 
    computed: {

        filteredMyList() {
      let filteredUsers = this.listOfMyShifts;
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
      if( this.filter.emergency == true){
        filteredUsers = filteredUsers.filter(shift => {
          shift.emergency == true
        })
      }
      if(this.filter.emergency == false){
        filteredUsers = filteredUsers.filter( shift => {
           shift.emergency == false
        })
      }

      
      return filteredUsers;
    }
    }


}
</script>

<style scoped>

</style>