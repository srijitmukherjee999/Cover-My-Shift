<template>
  
  <div>

  </div>
</template>



<script>
import ShiftService from '../services/ShiftService';
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

        }
    },
    created(){
        this.getAllShifts();
    }


}
</script>

<style>

</style>