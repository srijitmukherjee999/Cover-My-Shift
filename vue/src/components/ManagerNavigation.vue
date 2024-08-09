<template>
  <div class="navigation">
      <nav class="navigation">
        <ul>
          <li><router-link v-bind:to="{ name: 'manager' }">MY HOME</router-link></li>
          <li><router-link v-bind:to="{ name: 'pendingrequests' }">PENDING REQUESTS</router-link></li>
          <li><router-link v-bind:to="{ name: 'pickupshift' }">FIRE EMPLOYEE</router-link></li>
        </ul>
      </nav>
    </div>
</template>

<script>


export default {

}
</script>

<style>

.navigation {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    margin: 20px;
    border-radius: 5px;
    z-index: 2;
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

</style>