<template>
    <section>
      <div class="fixed-header">
        <company-header />
        <manager-greeting />
        <div id="backImage">
          <div class="overlay"></div>
          <div class="content">
            <manager-navigation />
  
            <!-- Hours per day input -->
            <div class="hours-input">
              <label for="requiredHours">Required Hours per Day:</label>
              <input
                type="number"
                v-model="requiredHours"
                @input="checkHours"
                placeholder="Enter hours"
              />
            </div>
  
            <div class="date-columns">
              <!-- Current Week -->
              <div class="date-column">
                <h3>Current Week</h3>
                <div
                  v-for="(date, index) in currentWeek"
                  :key="index"
                  :class="['bubble', isSufficientHours(date) ? 'green' : 'red']"
                  @click="navigateToAllShifts(date)"
                >
                  <p>{{ formatDate(date) }}</p>
                  <!-- <p>Total Hours: {{ totalHours[date] || 0 }}</p> -->
                </div>
              </div>
              <!-- Next Week -->
              <div class="date-column">
                <h3>Next Week</h3>
                <div
                  v-for="(date, index) in nextWeek"
                  :key="index"
                  :class="['bubble', isSufficientHours(date) ? 'green' : 'red']"
                  @click="navigateToAllShifts(date)"
                >
                  <p>{{ formatDate(date) }}</p>
                  <!-- <p>Total Hours: {{ totalHours[date] || 0 }}</p> -->
                </div>
              </div>
              <!-- Following Week -->
              <div class="date-column">
                <h3>Following Week</h3>
                <div
                  v-for="(date, index) in followingWeek"
                  :key="index"
                  :class="['bubble', isSufficientHours(date) ? 'green' : 'red']"
                  @click="navigateToAllShifts(date)"
                >
                  <p>{{ formatDate(date) }}</p>
                  <!-- <p>Total Hours: {{ totalHours[date] || 0 }}</p> -->
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </template>
  
  <script>
  import CompanyHeader from "../components/CompanyHeader.vue";
  import ManagerGreeting from "../components/ManagerGreeting.vue";
  import ManagerNavigation from "../components/ManagerNavigation.vue";
  import ShiftService from '../services/ShiftService';
  
  export default {
    data() {
      return {
        requiredHours: 8, // Default required hours per day
        currentWeek: [], 
        nextWeek: [], 
        followingWeek: [], 
        totalHours: {}, // Object to store total hours per date in yyyy-mm-dd format
      };
    },
    methods: {
        async getHours() {
  const totalHours = {};

  // Combine all weeks into one array
  const allDates = [...this.currentWeek, ...this.nextWeek, ...this.followingWeek];

  for (const date of allDates) {
    try {
      const response = await ShiftService.getShiftByDate(date);
      const shifts = response.data;

      if (Array.isArray(shifts)) {
        let totalDuration = 0;

        shifts.forEach((shift) => {
          const { duration } = shift;
          totalDuration += duration;
        });

        // Store the total duration for the date
        totalHours[date] = totalDuration;
      } else {
        console.error('Unexpected data format:', response.data);
      }
    } catch (error) {
      console.error('Error fetching shifts:', error);
    }
  }

  // Update the totalHours data property
  this.totalHours = totalHours;

  // Debugging log
  console.log('Total Hours:', totalHours);
},

async getDatesForNextTwoWeeks() {
  const today = new Date();
  today.setHours(0, 0, 0, 0); // Reset time to ensure accurate date calculations

  const formatLocalDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const addDatesToArray = async (array, startDate, days) => {
    for (let i = 0; i < days; i++) {
      const date = new Date(startDate);
      date.setDate(startDate.getDate() + i);
      const localDate = formatLocalDate(date);
      array.push(localDate);

      // Fetch and sum shifts for the date
      try {
        const response = await ShiftService.getShiftByDate(localDate);
        // Process data as needed
      } catch (error) {
        console.error('Error fetching shifts:', error);
      }
    }
  };

  // Calculate the start date of the current week (from today)
  const startOfWeek = new Date(today);
  const dayOfWeek = today.getDay(); // Sunday is 0, Monday is 1, etc.
  const daysToStartOfWeek = dayOfWeek === 0 ? 6 : dayOfWeek - 1; // Calculate days back to Monday
  startOfWeek.setDate(today.getDate() - daysToStartOfWeek); // Move to Monday of the current week

  // Ensure current week starts from today
  if (today < startOfWeek) {
    startOfWeek.setDate(today.getDate()); // Adjust if today is earlier than Monday
  }

  // Debugging logs
  console.log('Today:', formatLocalDate(today));
  console.log('Start of Week:', formatLocalDate(startOfWeek));

  // Current Week
  this.currentWeek = []; // Clear current week array
  await addDatesToArray(this.currentWeek, startOfWeek, 7); // 7 days in the current week

  // Next Week
  const nextWeekStart = new Date(startOfWeek);
  nextWeekStart.setDate(startOfWeek.getDate() + 7);
  this.nextWeek = []; // Clear next week array
  await addDatesToArray(this.nextWeek, nextWeekStart, 7);

  // Following Week
  const followingWeekStart = new Date(startOfWeek);
  followingWeekStart.setDate(startOfWeek.getDate() + 14);
  this.followingWeek = []; // Clear following week array
  await addDatesToArray(this.followingWeek, followingWeekStart, 7);

  console.log('Current Week:', this.currentWeek);
  console.log('Next Week:', this.nextWeek);
  console.log('Following Week:', this.followingWeek);
}

,
  
      formatLocalDate(date) {
        if (!(date instanceof Date)) {
            console.error('Invalid date object:', date);
            return '';
        }
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
        },

  
      isSufficientHours(date) {
        const localDate = this.formatLocalDate(new Date(date));
        const hours = this.totalHours[localDate] || 0;
        return hours >= this.requiredHours;
      },
  
      checkHours() {
        // This method is triggered when the required hours input changes
        this.$forceUpdate(); // Force re-render to update bubble colors
      },
  
      formatDate(date) {
        // Check if date is a string and try to convert it to a Date object
        if (typeof date === 'string') {
          date = new Date(date);
        }
  
        // Check if the resulting date is valid
        if (!(date instanceof Date) || isNaN(date.getTime())) {
          console.error('Invalid date object:', date);
          return '';
        }
  
        const options = {
          weekday: "long", // "Monday"
          month: "long", // "August"
          day: "numeric", // "20"
          year: "numeric", // "2024"
        };
  
        return date.toLocaleDateString("en-US", options);
      },

      navigateToAllShifts(date) {
      if (!(date instanceof Date)) {
        date = new Date(date); // Convert to Date object if it's not already
      }
      const formattedDate = date.toISOString().split('T')[0]; // Convert to YYYY-MM-DD
      this.$router.push({ name: 'allShifts', query: { date: formattedDate } });
    }
  },
    async created() {
      await this.getDatesForNextTwoWeeks();
      await this.getHours(); // Ensure hours are fetched after dates
    },
  
    mounted() {
      // Call getHours when component mounts
      this.getHours();
    },
  
    components: { CompanyHeader, ManagerGreeting, ManagerNavigation },
  };
  </script>
  





  
<style>
.date-columns {
  display: flex;
  justify-content: space-between;
  row-gap:10px;
}

.date-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.bubble {
  background-color: #4a90e2;
  color: white;
  border-radius: 50px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  margin-bottom: 10px;
  text-align: center;
  transition: background-color 0.3s;
}

.green {
  background-color: green;
}

.red {
  background-color: red;
}

.hours-input {
  margin-bottom: 20px;
  text-align: center;
}

.hours-input label {
  margin-right: 10px;
}

h3 {
  margin-bottom: 20px;
  color: #333;
}
</style>
