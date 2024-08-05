<template>
  <h1>Hi CJ</h1>
  <nav>
    <router-link v-bind:to="{name: 'employee'}">MY HOME</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'timeoff'}">REQUEST TIME OFF</router-link>
    &nbsp; | &nbsp;
    <router-link v-bind:to="{name: 'pickupshift'}">PICK UP SHIFT</router-link>
  </nav>
  <request-off-form/>
</template>

<script>
import RequestOffForm from '../components/RequestOffForm.vue'
export default {
    components: {
        RequestOffForm
    }

}
</script>

<style scoped>

di
RequestOffFormv {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
}

nav {
    text-align: center;
}

</style>