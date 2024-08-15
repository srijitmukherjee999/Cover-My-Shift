<template>
  <section>
    <div>
      <nav class="navigation">
        <ul>
          <li><router-link v-bind:to="{ name: 'manager' }">MY HOME</router-link></li>
          <li><router-link v-bind:to="{ name: 'pendingrequests' }">PENDING REQUESTS</router-link></li>
          <li><router-link v-bind:to="{ name: 'allShifts' }">ALL SHIFTS</router-link></li>
          <li><router-link v-bind:to="{ name: 'coverage' }">Coverage</router-link></li>
        </ul>
      </nav>
    </div>
  </section>
</template>

<script>
export default {};
</script>

<style >
html,
section {
  margin: 0;
  padding: 0;
}

section > div {
  margin: 0;
  padding: 0;
}
.navigation {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
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
  color: black;
  background-color: white;
  border-radius: 50px;
  padding: 20px;
  box-shadow: 0 4px 8px;
  width: 100%;
  transition: transform 0.3s, box-shadow 0.3s;
  font: bold;
  z-index: 3;
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