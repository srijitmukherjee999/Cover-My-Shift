<template>
    <div id="backImage">
      <div class="overlay"></div>
    <div class="content">
    <div class="inside">
    <router-link class="login" v-bind:to="{name: 'login'}">Login</router-link>
  </div>
    <h1 id="company">Cover My Shift</h1>
    
    <div class="text-center">
    <h3>About Cover My Shift</h3>

            <p>Welcome to Cover My Shift!

        At Cover My Shift, we understand that life happens. 
        Whether it’s an unexpected emergency, a family event, or just the need for a well-deserved break, 
        we’re here to ensure that your work schedule remains smooth and stress-free. 
        Our platform connects employers with qualified professionals who are ready to step in and cover shifts, 
        so you can maintain seamless operations without missing a beat.</p>

        <h3>Our Mission</h3>
        <p>Our mission is simple: to provide a reliable, efficient, and user-friendly solution for shift coverage. We believe that work-life balance is essential, 
          and our goal is to make it easier for both employers and employees to manage unexpected schedule changes with minimal hassle.</p>

          <h3>How It Works</h3>
          <p>For Employers: Post your shift needs on our platform, and our network of vetted professionals will be notified. 
            You can quickly review candidates, make your selection, and confirm coverage—all in a few clicks. 
            Say goodbye to the stress of last-minute staffing issues!

            For Employees: 
            Looking to pick up extra shifts or help out when someone’s in a pinch? 
            Browse available opportunities, apply for shifts that fit your schedule, and gain valuable work experience. 
            Our system ensures that you’re matched with shifts that suit your skills and availability.</p>


            <h3>Why Choose Us?</h3>


           <li> Ease of Use: Our intuitive platform makes it simple to post and apply for shifts. No complicated procedures, just straightforward solutions.</li>
           <li>Trusted Professionals: We carefully vet all our users to ensure you’re matched with qualified and reliable individuals.</li>
           <li>Flexibility: Whether you’re an employer needing urgent coverage or an employee seeking extra hours, our platform offers the flexibility you need.</li>

                <h3>Our Team</h3>
           <p>Cover My Shift was founded by a group of industry professionals who recognized the challenges of managing work schedules.
             With years of experience in staffing and operations, we’re passionate about creating a 
             solution that benefits both employers and employees alike.</p>

            </div> 
            </div> 
            </div> 
  
</template>

<script>
export default {






};
</script>


<style scoped>

.login{
    display: flex;
    justify-content: flex-end;
    justify-self: flex-end;
    background-color: white;
    padding: 20px; 
    font-size: 20px;
    font-weight: bold;
    color: #4a90e2;
    text-align: center;
    text-decoration: none;
    border-radius: 50px;
    width: 5%;
  
}

.login:hover {
    background: #d83a56;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
}

.login:focus {
    outline: none;
}


  #backImage {
  position: relative;
  height: 100%;
  background-image: url("..\assets\ben-o-bro-wpU4veNGnHg-unsplash.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8); 
  z-index: 0; 
}

.content {
  position: relative;
  z-index: 1; 
  padding: 20px;
}

#company {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  color: #4a90e2;
  width: 80%; 
  max-width: 1200px;
  margin: 2% auto; 
  font-size: 9rem; 
  box-sizing: border-box;
  text-shadow: 10px; 
  font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
}

@media (max-width: 1024px) {
  #company {
    width: 85%; 
    font-size: 1.75rem;
    padding: 20px; 
  }
}

@media (max-width: 768px) {
  #company {
    font-size: 1.5rem; 
    padding: 15px; 
  }
}

@media (max-width: 480px) {
  #company {
    font-size: 1rem; 
    padding: 10px; 
  }
}

  .text-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 20em;
  background-color: #4a90e2;
  box-shadow: 0 0 10px;
  border-radius: 50px;
  width: auto;
  max-width: 100%;
  margin-top: 5em;
  
}


.text-center h3 {
  font-size: 2rem;
  padding: 5px; 
}

</style>