<template>
    <div class="fixed-header">
    <CompanyHeader/>
    <!-- v-if="userRole === 'ROLE_MANAGER'" -->
    <ManagerGreeting/>
    <ManagerNavigation/>
    <div>
      <h1>Pending Cover Requests for Shift ID: {{ shiftId }}</h1>
      <ul>
        <li v-for="request in coverRequests" :key="request.id">
          Request by: {{ request.fullName }}
        </li>
      </ul>
    </div>
    </div>
  </template>
  
  <script>
    import CompanyHeader from '../components/CompanyHeader.vue';
    import ManagerGreeting from '../components/ManagerGreeting.vue';
    import ManagerNavigation from '../components/ManagerNavigation.vue';
    import ManagerService from '../services/ManagerService';

  export default {
    data() {
        return {
            shiftId: this.$route.params.shiftId,
            coverRequests: []
        };
    },
    methods: {
        getCoverRequests() {
            ManagerService.getCoverRequests(this.shiftId).then(response => {
                this.coverRequests = response.data;
            })
                .catch(error => {
                console.error("Error fetching cover requests:", error);
            });
        }
    },
    created() {
        this.getCoverRequests();
    },
    components: { CompanyHeader, ManagerGreeting, ManagerNavigation }
};
  </script>
  
  
    
  <style scoped>
  .yes {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    background-color: white;
  }
  
  .navigation {
    padding: 10px;
    margin: 20px;
    border-radius: 5px;
  }
  
  .navigation a {
    text-decoration: none;
    color: #000000;
  }
  
  .navigation ul {
    list-style: none;
    padding: 0;
    margin: center;
    text-align: center;
  }
  
  .navigation li {
    display: inline;
    margin-right: 15px;
    font-size: larger;
    background-color: white;
    color: black;
    border-radius: 50px;
    padding: 20px;
    box-shadow: 0 4px 8px;
    width: 100%;
    transition: transform 0.3s, box-shadow 0.3s;
    font: bold;
  }
  
  .navigation li:hover {
    transform: scale(1.05);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
    background-color: lightgray;
  }
  
  #data {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
    padding: 20px;
  }
  
  .bubble {
    background-color: #4a90e2;
    color: white;
    border-radius: 50px;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 10px;
    text-align: left;
    box-sizing: border-box;
    transition: transform 0.3s, box-shadow 0.3s;
    position: relative;
  }
  
  .bubble-title {
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .bubble-info {
    display: flex;
    flex-direction: column;
    gap: 5px;
  }
  
  .bubble-actions {
    display: flex;
    justify-content: space-between;
    gap: 10px;
  }
  
  .accept-button {
    background-color: #5cb85c;
    border: none;
    border-radius: 20px;
    padding: 10px 20px;
    cursor: pointer;
    color: white;
    font-weight: bold;
  }
  
  .accept-button:hover {
    background-color: #4cae4c;
  }
  
  .reject-button {
    background-color: #d9534f;
    border: none;
    border-radius: 20px;
    padding: 10px 20px;
    cursor: pointer;
    color: white;
    font-weight: bold;
  }
  
  .reject-button:hover {
    background-color: #c9302c;
  }
  </style>
  