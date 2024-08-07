<template>
    <body>
    <div id="form-body">
  <form class="request-off-form" v-on:submit.prevent="addNewRequest" v-if="showForm">
      <input type="date" class="start-date-input" placeholder="Date" v-model="newRequest.startDate">
      <input type="date" class="end-date-input" placeholder="Date" v-model="newRequest.endDate">
      <input type="text" class="description-input" placeholder="description" v-model="newRequest.description">

      <button type="submit" >Submit Request</button>


    </form>
    <button v-else @click="toggleForm">Request Time Off</button>
</div>
</body>
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
            status: 1,
        },

    };

},

methods:{

    addNewRequest(){
        ShiftService.sendTimeOffRequest(this.newRequest).then(response => {
            if(response.status === 201){
                alert("Pending Management Review")
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

body, html {
      height: 100%;
      margin: 0;
      display: flex;
      justify-content: center; 
      align-items: center;     
    }

#form-body {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

form {
    display: flex;
  flex-direction: column;
  align-items: center;
  align-content: center;
  gap: 20px;
}

.request-off-form {
    background-color: orange; 
  color: white;
  border-radius: 50px; 
  padding: 35px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); 
  width: 150%;
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  justify-content: center;
  align-content: center;  
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;    
}

input[type="text"] {
  width: 400px;         
  height: 50px;         
  padding: 10px;        
  font-size: 18px;      
  border: 2px solid #000; 
  border-radius: 5px; 
  text-align: center; 
}

input[type="date"] {
  width: 400px;         
  height: 50px;         
  padding: 10px;        
  font-size: 18px;      
  border: 2px solid #000; 
  border-radius: 5px;
  text-align: center;  
}

button[type="submit"] {
  width: 400px;         
  height: 50px;         
  padding: 10px;        
  font-size: 18px;      
  border: 2px solid #000; 
  border-radius: 5px; 
  font-size: large;
}

button:hover {
    transform: scale(1.05); 
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  background-color: lightgray;
}
</style>