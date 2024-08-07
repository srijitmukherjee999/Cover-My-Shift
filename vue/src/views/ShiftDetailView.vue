<template>
  
  <h1>Hello {{ name }}</h1>

    <div class ="uncover">

    <button @click="updateShiftStatusToUncovered">Uncover</button>
        </div>
        <div class="accept">
        <button @click="updateShiftStatusToAssigned">Accept</button>

    </div>

</template>



<script>
import ShiftService from '../services/ShiftService';
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

    ShiftService.updateUserShiftStatus(this.$route.params.id,3).then(response => {
    if(response.status === 200){
        alert("You have updated");
    }
})

},
    updateShiftStatusToAssigned(){
        ShiftService.updateUserShiftStatus(this.$route.params.id,2).then(response => {
            if(response.status === 200){
                alert("You have updated");
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