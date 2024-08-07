<template>
  <form class="request-off-form" v-on:submit.prevent="addNewRequest" v-if="showForm">
      <input type="date" class="start-date-input" placeholder="Date" v-model="newRequest.startDate">
      <input type="date" class="end-date-input" placeholder="Date" v-model="newRequest.endDate">
      <input type="text" class="description-input" placeholder="description" v-model="newRequest.description">
      <button type="submit" >Submit Request</button>
    </form>
    <button v-else @click="toggleForm">Request Time Off</button>
    
</template>

<script>

import ShiftService from '../services/ShiftService';

export default {
    data() {
        return {
        showForm: true,
        newRequest: {
            startDate: '',
            endDate: '',
            description: '',
            status: 1
        },

    };

},

methods:{

    addNewRequest(){
        ShiftService.sendTimeOffRequest(this.newRequest).then(response => {
            if(response.status === 201){
                alert("Pending Management ")
            }
        });
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

.request-off-form {
    display: flex;
    flex-direction: column;
    max-width: 50%;
    margin-left : 25%
    
}


</style>