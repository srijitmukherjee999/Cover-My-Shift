<template>
  
  <h1>Hello {{ name }}</h1>
    <div>

        <button>Uncover</button>

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

updateShiftStatus(status){

ShiftService.updateUserShiftStatus(status).then(response => {
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

<style>

</style>