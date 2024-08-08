<template>
  <company-header/>
  <employee-greeting/>
  <employee-navigation/>  
  <request-off-form/>


  <div v-for="shift in listOfPendingRequests" v-bind:key="shift.shiftId">

    <p>{{ shift.assignedName }}</p>&nbsp;&nbsp;
    <p>{{ shift.startDateTime }}</p>&nbsp;&nbsp;
    <p>{{ shift.duration }}</p>&nbsp;&nbsp;
    <p>{{ convertStatus(shift.status) }}</p>&nbsp;&nbsp;
    <p>{{ shift.emergency }}</p>&nbsp;&nbsp;


  </div>





</template>

<script>
import CompanyHeader from '../components/CompanyHeader.vue';
import EmployeeGreeting from '../components/EmployeeGreeting.vue';
import EmployeeNavigation from '../components/EmployeeNavigation.vue';
import RequestOffForm from '../components/RequestOffForm.vue'
import ShiftService from '../services/ShiftService';
export default {
  data(){
    return {
      name: '',

    listOfPendingRequests: [
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
    ]
    }
    
  },
    components: {
        RequestOffForm,
        CompanyHeader,
        EmployeeGreeting,
        EmployeeNavigation
    },
    methods: {
              getFullName(){

          ShiftService.getUserFullName().then( response => {

                this.name = response.data;

                this.$store.commit("ADD_NAME", this.name);
        })

        },

        getMyShiftPendingRequests(){
          ShiftService.getMyShiftsByUncoveredRequest(2).then( response => {

              this.listOfPendingRequests = response.data;

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



    },
    created(){
      this.getFullName();
      this.getMyShiftPendingRequests();
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

.navigation a {
  text-decoration: none;
  color: #000000;
}

.navigation ul {
    list-style: none;
    padding: 0;
    margin: 0;
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

@media (max-width: 768px) {
    .navigation li {
        font-size: medium; 
        padding: 15px; 
    }
}

@media (max-width: 480px) {
    .navigation li {
        font-size: small; 
        padding: 10px; 
    }

    .navigation ul {
        display: flex;
        flex-direction: column;
        flex-wrap: wrap; 
    }
}

.request-off-form {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
}


h1{
  font-style: italic;
  font-weight: bold;
  animation: fadeIn 2s;
}

p{

  display: flex;
  display: inline-block;
  justify-content: center;
}


</style>