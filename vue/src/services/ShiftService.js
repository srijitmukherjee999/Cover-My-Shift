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

   updateUserShiftStatus(status){

      return axios.put(`/shift/:id?status=${status}`);

   }


  
  }