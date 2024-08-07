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


   getMyShifts(mine){

      return axios.get(`/shifts?mine=${mine}`);

   },

   getEmergencyShifts(emergency,status){

      return axios.get(`/shifts?emergency=${emergency}&status=${status}`);
   }


  
  }