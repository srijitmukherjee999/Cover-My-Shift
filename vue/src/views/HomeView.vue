<template>
  <div class="home">
    <div id="backImage">
    <div class="inside">
    <h1>Home</h1>
    <router-link class="login" v-bind:to="{name: 'login'}">Login</router-link>
  </div>
    <h2>Cover My Shift</h2>
    
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
</template>

<script>
export default {






};
</script>


<style scoped>

  .login{

    display: flex;
  

    justify-self: flex-end;

  }


  .inside{

    display: flex;
    justify-content: space-between;

  }

  #backImage {
  position: relative;
  height: 100vh;
  background-image: url("..\assets\ben-o-bro-wpU4veNGnHg-unsplash.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}

</style>