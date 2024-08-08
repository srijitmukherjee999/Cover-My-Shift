<template>
  <body>
    <div class="yes">
         <h1>Hello {{ name }}</h1>
        <h1>Hours Worked: 40</h1>
 </div>
  </body>
</template>

<script>

import ShiftService from '../services/ShiftService';

export default {

}
</script>

<style scoped>

.yes{
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
    padding: 10px;
    background-color: white;
}


</style>