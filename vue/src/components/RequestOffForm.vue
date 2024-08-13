<template>
  <section>
    <div id="form-body">
  <form class="request-off-form" v-on:submit.prevent="addNewRequest" v-if="showForm">
      <input type="date" class="start-date-input" placeholder="Date" v-model="newRequest.startDate">
      <input type="date" class="end-date-input" placeholder="Date" v-model="newRequest.endDate">
      <input type="text" class="description-input" placeholder="description" v-model="newRequest.description">
      <button type="submit" >Submit Request</button>
    </form>
    
    <button class="requestOffButton" v-else @click="toggleForm">Request Time Off</button>
    
</div>
</section>
</template>

<script>
import ShiftService from '../services/ShiftService.js';



export default {
    data() {
        return {
        showForm: true,
        newRequest: {
            startDate: '',
            endDate: '',
            description: '',
            status: 1,
        },

    };

},

methods: {
    addNewRequest() {

        const vacationRequest = {
            employeeId: this.$store.state.user.id,
            startDate: this.newRequest.startDate,
            endDate: this.newRequest.endDate,
            status: 1,
            description: this.newRequest.description
        };

        ShiftService.createVacationRequest(vacationRequest).then(response => {
            if (response.status === 201) {
                alert("Vacation request submitted and is pending management review.");
                this.clearForm();
            } else {
                alert("There was an error submitting your request.");
            }
        }).catch(error => {
            console.error("Error creating vacation request:", error);
            alert("There was an error submitting your request.");
        });
    },
    clearForm() {
        this.newRequest = {
            startDate: '',
            endDate: '',
            description: ''
        };
        this.showForm = false;
    },
    toggleForm() {
        this.showForm = !this.showForm;
    }
}


}
</script>

<style scoped>

#form-body {
  padding: 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

form {
    display: flex;
  flex-direction: row;
  align-items: center;
  align-content: center;
  gap: 20px;
}

.request-off-form {
    background-color: orange; 
  color: white;
  border-radius: 50px; 
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-content: center; 
  text-align: center; 
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
  gap: 20px;    
}

input[type="text"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}


input[type="date"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center; 
}

button[type="submit"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}

button:hover {
    transform: scale(1.05); 
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  background-color: lightgray;
}

.requestOffButton {
    display: inline;
    margin: auto;
    font-size: larger;
    color: Orange;
    font-weight: bold;
    border-radius: 50px;
    padding: 20px;
    box-shadow: 0 4px 8px;
    max-width: 100%; 
    transition: transform 0.3s, box-shadow 0.3s;
    font: bold;
    border-color: orange;
    border-width: 20px;
}
</style>