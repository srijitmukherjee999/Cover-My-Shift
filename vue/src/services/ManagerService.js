import axios from "axios";

export default{
    acceptRejectRequest(approvalDto){
        return axios.put('/manage/shifts', approvalDto, { method: 'PUT'})
    },

    acceptDayOffRequest(id,status){
        return axios.put( `/shifts/${id}?status=${status}`);
    },

    getCoverRequests(shiftId,status){
        return axios.get(`/shift/${shiftId}/cover?status=${status}`);
    },
    

    getManagerViewCoverRequests(shiftId){

        return axios.get(`shift/${shiftId}/cover`);
    },
      
    getVacationRequests(){
        return axios.get('/vacations');
    },

    acceptRejectVacationRequest(vacationId, status){
        return axios.put(`/vacation/${vacationId}?status=${status}`);
    },
    
    getListOfVacations(){
        return axios.get(`/vacations`);
    }
}