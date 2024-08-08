import axios from "axios";

export default{
    acceptRejectRequest(approvalDto){
        return axios.put('/manage/shifts', approvalDto)
    }
      
}