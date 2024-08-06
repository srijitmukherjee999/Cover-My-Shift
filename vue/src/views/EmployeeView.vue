<template>
     <body>
        
    <div class="yes">
         <h1>Hello User</h1>
        <h1>Hours Worked: 40</h1>
 
  
    
    </div>
  <nav class="navigation">
    <ul>
        <li><router-link v-bind:to="{name: 'employee'}">MY HOME</router-link></li>
    
        <li><router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link></li>
    
        <li><router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link></li>
    </ul>
  </nav>
  
  </body>   
</template>

<script>
import ShiftService from '../services/ShiftService';
import RequestTimeOffView from './RequestTimeOffView.vue';


export default {

    data(){
        
        return{
            listOfShifts : [
                {
                    name : '',
                    assigned : 0,
                    userId: 0,
                    startDateTime: '',
                    duration: 0,
                    status: 0,
                    emergency:false,
                    covererId: 0,
                    description: ''
                    
                }
            ],
            name: []
        }
    }
    ,

    methods:{
        getAllShifts(){
            ShiftService.getShifts().then(response => {
               this.listOfShifts = response.data;
               this.getAllShifts();
            })
        },

        getUser(id){
            ShiftService.getUserByUserId(id).then(response => {
              this.$store.state.user = response.data;
            })
        },
        getNameByShift(){

            this.listOfShifts.forEach(e => {
              this.name = this.getUser(e.Id).name;
            })

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

body{

    background-image: url( "../assets/AdobeStock_440299419_Preview.jpeg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    min-height:100vh;
}

</style>