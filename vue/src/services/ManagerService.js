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
    }
      
}