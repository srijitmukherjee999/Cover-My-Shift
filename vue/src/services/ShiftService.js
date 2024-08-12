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

      return axios.get(`/shifts?mine=${mine}`);
   },
   
   deleteUserShift(id){
      return axios.delete(`/shift/${id}`)
   },
   
   getEmergencyShifts(emergency,status){

      return axios.get(`/shifts?emergency=${emergency}&status=${status}`);
   },

   updateShiftStatus(id,status){

      return axios.put(`/shift/${id}?status=${status}`);

   },

   getMyShiftsByUncoveredRequest(assigned,status){

      return axios.get(`/shifts?assigned=${assigned}&status=${status}`);

   },

   getShiftsByUncoveredRequest(status){

         return axios.get(`/shifts?status=${status}`);

   },

   createCoverRequest(id){

         return axios.post(`/shift/${id}`);

   }



}

  

