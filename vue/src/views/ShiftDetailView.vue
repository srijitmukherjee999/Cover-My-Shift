<template>
  
  <div class="yes">
        <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: 40</h1>

    <div class ="uncover">
        <button @click="updateShiftStatusToUncovered">Request Day Off</button>
    </div>
    
    <div class="accept">
        <button @click="coverThisShift">Cover this Shift</button>

    </div>
  </div>
</template>



<script>
import ShiftService from '../services/ShiftService.js';
export default {
    data(){
        return{
            name:''
        }
    },
    methods: {
        getFullName(){

    ShiftService.getUserFullName().then( response => {

       this.name = response.data;

      this.$store.commit("ADD_NAME", this.name);
})

},

    updateShiftStatusToUncovered(){

    ShiftService.updateShiftStatus(this.$route.params.id,2).then(response => {
    if(response.status === 200){
        alert("You have requested the day off. Pending Management decision");
        this.$router.push('/employee');
    }
})

},
    coverThisShift(){

        ShiftService.createCoverRequest(this.$route.params.id).then(response => {
            if(response.status === 201){
                alert("You have requested to cover this shift.Pending Management decision");
                this.$router.push('/employee');
            }
        })

    }


    },
    created(){
        this.getFullName();
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


.uncover{
    display: flex;
    justify-content: center;
}
.accept{
    display: flex;
    justify-content: center;
}

h1{
    display: flex;
    justify-content: center;
}
</style>