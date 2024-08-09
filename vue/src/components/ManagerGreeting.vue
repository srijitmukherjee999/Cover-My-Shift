<template>
  <div class="yes">
      <h1>Hello Manager {{ name }}</h1>
    </div>
</template>

<script>
import ShiftService from '../services/ShiftService';

export default {

}
</script>

<style scoped>

.yes {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    background-color: white;
  }

</style>