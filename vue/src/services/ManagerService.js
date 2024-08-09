import axios from "axios";

export default{
    acceptRejectRequest(approvalDto){
        return axios.put('/manage/shifts', approvalDto)
    },



    acceptDayOffRequest(id,status){
        return axios.put( `/shifts/${id}?status=${status}`);
    }
      
}