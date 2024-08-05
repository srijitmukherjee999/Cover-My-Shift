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
                    fullName : '',
                    id : 0,
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
    },


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
              this.name = this.getUser(e.userId).fullName;
            })

        }
    }


}
</script>

<style>

</style>