<template>
  <body>
    <div class="yes">
         <h1>Hello {{ name }}</h1>
 </div>
  <div>
    <div id="data" v-for="user in listOfUsers" :key="user.id">

      <div class="bubble">
        <p class="bubble-title">{{ user.fullName }}</p>
      </div>

  </div>
  </div>
  </body>
</template>



<script>
import ShiftService from '../services/ShiftService';
import AuthService from '../services/AuthService';

export default {

    data(){
        return{
            listOfShifts : [
                {
                    name : '',
                    shiftId : 0,
                    assigned: 0,
                    startDateTime: '',
                    duration: 0,
                    status: 0,
                    emergency:false,
                    coverer: 0,
                    description: ''
                    
                }
            ],
            listOfUsers : [
                {
                    id: 0,
                    username: "",
                    fullName: "",
                    authorities: [
                        {
                            name: ""
                        }
                    ]
                }
            ],
            name: []
        }
    },


    methods:{
        getAllShifts(){
            ShiftService.getShifts().then(response => {
               this.listOfShifts = response.data;
               this.getNameByShift();
            })
        },

        getUser(id){
            ShiftService.getUserByUserId(id).then(response => {
              this.$store.state.user = response.data;
            })
        },

        getNameByShift(){
            this.listOfShifts.forEach(e => {
              this.name = this.getUser(e.id).name;
            })
        },

        getAllUsers(){
            AuthService.getUsers().then(response => {
                this.listOfUsers = response.data;
            })
        },


    },
    created(){
        this.getAllShifts();
        this.getAllUsers();
    }


}
</script>

<style scoped>
body, html {
      height: 100%;
      margin: 0;
      display: flex;
      justify-content: center; 
      align-items: center;     
    }

    .yes{
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    background-color: white;
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

.bubble-title {
  font-weight: bold; 
  margin-bottom: 10px; 
}


</style>