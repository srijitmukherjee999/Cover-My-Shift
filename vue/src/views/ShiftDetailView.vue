<template>
  
  <h1>Hello {{ name }}</h1>
    <div></div>

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
    },
    created(){
        this.getFullName();
    }

}
</script>

<style>

</style>