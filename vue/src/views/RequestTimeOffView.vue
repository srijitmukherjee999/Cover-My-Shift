<template>
  <h1>Hi CJ</h1>
  <nav>
    <router-link v-bind:to="{name: 'employee'}">MY HOME</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link>
  </nav>
</template>

<script>
export default {

}
</script>

<style>

</style>