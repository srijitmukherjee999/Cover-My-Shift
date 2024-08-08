<template>
    <body>
        <div>
    <nav class="navigation">
    <ul>
       <li><router-link v-bind:to="{name: 'employee'}">MY HOME</router-link></li>
    
        <li><router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link></li>
    
        <li><router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link></li>

        <li><router-link v-bind:to="{name: 'myshift'}">MY SHIFT</router-link></li>
    </ul>
  </nav>
</div>
    </body>
</template>

<script>
export default {

}
</script>

<style scoped>

body {
    background: transparent;
}

html, body {
  margin: 0;
  padding: 0;
}

body > div {
  margin: 0;
  padding: 0;
}
.navigation {
    padding: 0;
    margin: 20px;
    border-radius: 5px;
}

.navigation a {
  text-decoration: none;
  color: #000000;
}

.navigation ul {
    padding: 0;
    margin: 0;
    text-align: center;
}

.navigation li {
    display: inline;
    margin-right: 15px;
    font-size: larger;
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

@media (max-width: 768px) {

    .navigation li {
        font-size: medium; 
        padding: 15px; 
    }
}

@media (max-width: 480px) {

 
    .navigation li {
        font-size: small; 
        padding: 10px; 
        display: flex;
        flex-direction: column;
        flex-wrap: wrap; 
    }
    

    .navigation ul {
        display: flex;
        flex-direction: column;
        flex-wrap: wrap; 
    }
}

</style>