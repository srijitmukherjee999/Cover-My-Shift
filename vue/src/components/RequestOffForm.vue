<template>
  <form class="request-off-form" v-on:submit.prevent="addNewRequest" v-if="showForm">
      <input type="date" class="start-date-input" placeholder="Date" v-model="newRequest.startdate">
      <input type="date" class="end-date-input" placeholder="Date" v-model="newRequest.enddate">
      <button type="submit">Submit Request</button>
    </form>
    <button v-else @click="toggleForm">Request Time Off</button>
    
</template>

<script>
export default {
    data() {
        return {
        showForm: true,
        newRequest: {
            fullName : this.$store.state.user.fullName,
        
        },
    };

},

methods:{

    addNewRequest(){
        this.$store.commit("ADD_REQUEST",this.newRequest);
         this.clearForm();
    },
    clearForm(){
        this.newRequest = {}
        this.showForm = false;
    },
    toggleForm(){
        this.showForm = !this.showForm;
    }
    


}

}
</script>

<style scoped>

form{
    text-align: center;
    padding: 50px;
    border: 5px;
    border-color: orange;
}


</style>