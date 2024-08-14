<template>
    <div>
      <vue-cal
        :events="calendarEvents"
        :cellStyle="cellStyle"
        @date-click="handleDateClick"
      ></vue-cal>
    </div>
  </template>
  
  <script>
  import VueCal from 'vue-cal';
  import 'vue-cal/dist/vuecal.css';
  import ShiftService from '../services/ShiftService';
  
  export default {
    components: { VueCal },
    data() {
      return {
        // Example events data, you should replace this with your actual data
        calendarEvents: [
          { start: '2024-08-20', end: '2024-08-20', title: 'Shift', hours: 8 },
          { start: '2024-08-21', end: '2024-08-21', title: 'Shift', hours: 32 },
          { start: '2024-08-22', end: '2024-08-22', title: 'Shift', hours: 24 }
        ]
      };
    },
    methods: {
      // Example function to calculate total hours for a given day
      calculateTotalHours(date) {
        // Aggregate hours for the given date
        const dayEvents = this.calendarEvents.filter(event => event.start === date);
        return dayEvents.reduce((total, event) => total + event.hours, 0);
      },
      // Function to determine cell style based on hours
      cellStyle(date) {
        const totalHours = this.calculateTotalHours(date);
        const color = totalHours >= 32 ? 'green' : 'red';
  
        return {
          backgroundColor: color,
          color: 'white',
          fontWeight: 'bold'
        };
      },
      handleDateClick(date) {
        console.log('Date clicked:', date);
      }
    }
  };
  </script>
  
  <style>
  /* Additional styles if needed */
  </style>
  