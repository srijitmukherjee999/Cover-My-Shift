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

  <div id="data" v-for="shift in listOfShifts" v-bind:key="shift">
    
    <div><p>Name</p><br> {{ shift.assignedName }}</div>
    <div><p>Start Time</p><br> {{ shift.startDateTime}}</div>
    <div><p>Duration</p><br> {{ shift.duration }} hours</div>
    <div><p>Status</p><br> {{ convertStatus(shift.status) }} </div>
    <div><p>Emergency</p><br> {{ shift.emergency }}</div>

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
            name: ''
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

        }
    },
    created(){
        this.getAllShifts();
        this.getFullName();
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
    /* border: 2px solid white; */
    padding: 10px;
    margin: 20px;
    border-radius: 5px; 
    /* background-color: white; */
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
    background-color: blue;
    color: white;
    min-width: 20%;
    min-height: 30vh;
    gap: 20px;
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    border-radius: 50%;

}


@media (min-width: 600px) {
  #data {
    width: 100px; 
    height: 100px;
    font-size: 1rem; 
  }
}

@media (min-width: 900px) {
  #data {
    width: 150px; 
    height: 150px; 
    font-size: 1.2rem;
  }
}





</style>