<template>
  <h1>Hello {{name}}</h1>
  <nav>
    <router-link v-bind:to="{name: 'employee'}">MY HOME</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link>
  </nav>

  <div id="data" v-for="shift in listOfShiftsByStatus" v-bind:key="shift">
    
    <p>Name {{ shift.assignedName }}</p> &nbsp;
    <p>Start Time {{ shift.startDateTime}}</p>&nbsp;
    <p>Duration {{ shift.duration }} hours</p>&nbsp;
    <p>Status {{ convertStatus(shift.status) }} </p>&nbsp;
    <p> Emergency {{ shift.emergency }}</p>
   


  </div>
</template>


<script>
import ShiftService from '../services/ShiftService';
export default {


  data(){
    return{
      listOfShiftsByStatus: [ {

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
      }],
      name:''
    }
    
  },
  methods: {

    getShifts(status){
      
      ShiftService.getShiftByStatus(status).then(response => {
         this.listOfShiftsByStatus = response.data;
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
        getFullName(){

    ShiftService.getUserFullName().then( response => {

       this.name = response.data;

      this.$store.commit("ADD_NAME", this.name);
})

},

  }, 
  created(){
    this.getShifts(3);
    this.getFullName();
  }


}
</script>

<style scoped>

p{
  display: flex;
  justify-content: center ;
  
}

nav{
  display: flex;
  justify-content: center;
}
h1{
  display: flex;
  justify-content: center;
}
div{
  display: flex;
  justify-content: center;

}

</style>