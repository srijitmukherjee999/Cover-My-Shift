<template>
    <div>
  <h1>Hello User</h1>
  <h1>Hours Worked: 40</h1>
</div>

  <nav>
    <router-link v-bind:to="{name: 'employee'}">MY HOME</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link>
  </nav>

  <table>
    <thead>
        <td>date</td>
        <td>day</td>
        <td>start time</td>
        <td>end time</td>
    </thead>
        <tr>
            <td>1</td>
            <td>2</td>
            <td>3</td>
            <td>4</td>
        </tr>
    



  </table>
</template>

<script>
import RequestTimeOffView from './RequestTimeOffView.vue';

export default {

}
</script>

<style scoped>

div {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}


</style>