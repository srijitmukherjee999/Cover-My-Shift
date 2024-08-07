import axios from "axios";

export default {

   getShifts(){
   return  axios.get('/shifts');
   },

   getUserByUserId(id){
    return axios.get(`/user/${id}`);
   },

   sendTimeOffRequest(vacation){

    return axios.post('/vacation', vacation);

   },

   getShiftByStatus(status){

      return axios.get(`/shifts?status=${status}`);
   },

   getUserFullName(){
      return axios.get(`/shifts/username`);
   },

   updateUserShiftStatus(id,status){

      return axios.put(`/shift/${id}?status=${status}`);
   },

   createShift(shift){
      return axios.post(`/shift`, shift);
   },

   getMyShifts(mine){

<<<<<<< HEAD
   },
   
   deleteUserShift(id){
      return axios.delete(`/shift/${id}`)
   }


=======
      return axios.get(`/shifts?mine=${mine}`);

   },

   getEmergencyShifts(emergency,status){

      return axios.get(`/shifts?emergency=${emergency}&status=${status}`);
   }



  
>>>>>>> 82ff2f79ca3f3a0c854fc557102b6e5ac9d39d0b
  }