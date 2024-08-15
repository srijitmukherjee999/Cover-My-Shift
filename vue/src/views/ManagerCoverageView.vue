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
          <div id="hours-search">
          <div class="hours-input">
            <label class="bubble-title" for="requiredHours">Required Hours per Day:</label>
            <input
              type="number"
              v-model="requiredHours"
              @input="checkHours"
              placeholder="Enter hours"
            />
          </div>
        </div>
        </div>

        <div class="scrollable-container">
          <div class="scrollable-content">
            <div class="content">
              <div class="date-columns">
                <!-- Current Week -->
                <div class="date-column">
                  <h3>Current Week</h3>
                  <div
                    v-for="(date, index) in currentWeek"
                    :key="index"
                    :class="[
                      'bubble',
                      isSufficientHours(date) ? 'green' : 'red',
                    ]"
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
                    :class="[
                      'bubble',
                      isSufficientHours(date) ? 'green' : 'red',
                    ]"
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
                    :class="[
                      'bubble',
                      isSufficientHours(date) ? 'green' : 'red',
                    ]"
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
      </div>
    </div>
  </section>
</template>
  
  <script>
import CompanyHeader from "../components/CompanyHeader.vue";
import ManagerGreeting from "../components/ManagerGreeting.vue";
import ManagerNavigation from "../components/ManagerNavigation.vue";
import ShiftService from "../services/ShiftService";

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
      const allDates = [
        ...this.currentWeek,
        ...this.nextWeek,
        ...this.followingWeek,
      ];

      for (const date of allDates) {
        try {
          const response = await ShiftService.getShiftByDate(date);
          const shifts = response.data;

          if (Array.isArray(shifts)) {
            let totalDuration = 0;

            shifts.forEach((shift) => {
              if (shift.status != 3) {
                // if shift is not uncovered
                totalDuration += shift.duration;
              }
            });

            // Store the total duration for the date
            totalHours[date] = totalDuration;
          } else {
            console.error("Unexpected data format:", response.data);
          }
        } catch (error) {
          console.error("Error fetching shifts:", error);
        }
      }

      // Update the totalHours data property
      this.totalHours = totalHours;

      // Debugging log
      console.log("Total Hours:", totalHours);
    },

    async getDatesForNextTwoWeeks() {
      const today = new Date();
      today.setHours(0, 0, 0, 0); // Reset time to ensure accurate date calculations

      const formatLocalDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
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
            console.error("Error fetching shifts:", error);
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
      console.log("Today:", formatLocalDate(today));
      console.log("Start of Week:", formatLocalDate(startOfWeek));

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

      console.log("Current Week:", this.currentWeek);
      console.log("Next Week:", this.nextWeek);
      console.log("Following Week:", this.followingWeek);
    },

    formatLocalDate(date) {
      if (!(date instanceof Date)) {
        console.error("Invalid date object:", date);
        return "";
      }
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are zero-based
      const day = String(date.getDate()).padStart(2, "0");
      return `${year}-${month}-${day}`;
    },

    isSufficientHours(date) {
      const hours = this.totalHours[date] || 0;
      return hours >= this.requiredHours;
    },

    checkHours() {
      // This method is triggered when the required hours input changes
      this.$forceUpdate(); // Force re-render to update bubble colors
    },

    formatDate(date) {
      // Check if date is a string and try to convert it to a Date object
      if (typeof date === "string") {
        date = new Date(date);
      }

      // Check if the resulting date is valid
      if (!(date instanceof Date) || isNaN(date.getTime())) {
        console.error("Invalid date object:", date);
        return "";
      }

      const options = {
        weekday: "long", // "Monday"
        month: "long", // "August"
        day: "numeric", // "20"
        year: "numeric", // "2024"
        timeZone: "UTC",
      };

      return date.toLocaleDateString("en-US", options);
    },

    navigateToAllShifts(date) {
      this.$router.push({ name: "allShifts", query: { date: date } });
    },
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
  





  
<style scoped>

#hours-search {
  margin: 0 auto;
  width: 80%;
  max-width: fit-content;
  position: relative;
  z-index: 2;
  /* Ensure it is above the overlay but below scrolling content */
}

.hours-input {
  background-color: orange;
  color: white;
  border-radius: 50px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  text-align: center;
  box-sizing: border-box;
  transition: transform 0.3s, box-shadow 0.3s;
  gap: 20px;
}

input[type="number"] {
  width: 200px;
  height: 50px;
  padding: 10px;
  font-size: 18px;
  border: 2px solid #000;
  border-radius: 5px;
  text-align: center;
}
.date-columns {
  display: flex;
  justify-content: space-between;
  row-gap: 10px;
}

.date-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  font-weight: bold;
  padding: 20px;
  border-radius: 5px;
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
  color: white;
}

.bubble-title {
  font-weight: bold;
  flex: 1 1 auto;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}

#backImage {
  position: relative;
  height: 100vh;
  background-image: url("../assets/arlington-research-kN_kViDchA0-unsplash (1).jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  background-attachment: fixed; /* Fixed by default */
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  z-index: 1;
  /* Less than header */
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: white;
  z-index: 2;
}

.scrollable-container {
  position: fixed;
  top: 21em;
  /* Adjust this based on your header height */
  left: 0;
  right: 0;
  bottom: 0;
  overflow: auto;
  z-index: 1; /* Less than header */
}

.scrollable-content {
  height: 100%;
  overflow: auto;
  padding: 10px;
}

.content {
  position: relative;
  z-index: 1; /* Make sure it's behind the fixed header */
}

section {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

@media (max-width: 600px) {

section {
  
  overflow: auto; /* Enable scrolling */
  
}

h3 {
  margin-bottom: 20px;
  color: black;
  display: flex;
  align-content: center;
  text-align: center;
}

.fixed-header {
  position: relative;
  overflow: auto;
  height: 100vh;
}


.scrollable-container {
  position: relative; /* Adjust positioning to allow scrolling */
  overflow: auto; /* Allow scrolling */
  top: 0; /* Reset top position */
  padding-bottom: 120px;
  margin-bottom: 50px;
}
#hours-search {
  display: flex;
  width: 65%; /* Make the search shifts section take more width on smaller screens */
  padding: 20px;
  z-index: 2;
}

.data-columns {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
  text-align: center;
}

.filter {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
}

input[type="text"], input[type="date"], select, [type="button"] {
  width: 100%;
  height: auto; /* Adjust height for better fit */
  margin-bottom: 2px; /* Add margin for spacing between elements */
}

.bubble {
  flex-direction: column; /* Stack elements vertically in smaller screens */
  padding: 20px;
}

.bubble-title {
  font-size: 16px; /* Smaller text size */
  padding: 10px;
}

#backImage {
background-attachment: scroll;
background-repeat: repeat;
background: transparent;
height: 100%;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  z-index: 1;
  overflow: auto;
}
}

/* For even smaller screens */
@media (max-width: 400px) {

section {
  overflow: auto; /* Enable scrolling */
  height: 100%;
}

h3 {
  margin-bottom: 20px;
  color: black;
}

.content {
  position: relative;
  z-index: 1; /* Make sure it's behind the fixed header */
  overflow: auto;
}

.fixed-header {
  overflow: auto;
}
.bubble-title {
  font-size: 14px; /* Further reduce text size */
}

.filter input[type="text"], 
.filter input[type="date"], 
.filter select, 
.filter [type="button"] {
  font-size: 16px; /* Adjust font size for better readability */
}

#backImage {
  background-attachment: scroll;
  background-repeat: repeat; /* Ensure scroll behavior on very small screens */
  overflow: auto;
  background: transparent;
  height: 100%;
}

.overlay {
position: absolute;
top: 0;
left: 0;
width: 100%;
height: 100%;
background: transparent;
z-index: 0;
overflow: auto;
}

.scrollable-container {
  position: relative; /* Adjust positioning to allow scrolling */
  overflow: auto; /* Allow scrolling */
  top: 0; /* Reset top position */
  padding-bottom: 120px;
}
}
</style>
