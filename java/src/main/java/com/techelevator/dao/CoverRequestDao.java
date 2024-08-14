package com.techelevator.dao;

import com.techelevator.model.CoverRequest;

import java.util.List;

public interface CoverRequestDao {

    CoverRequest getCoverRequest(int shiftId, int employeeId);
    void createCoverRequest(int shiftId, int employeeId);
    void deleteCoverRequest(int shiftId, int employeeId);
    CoverRequest updateCoverRequest (CoverRequest coverRequest);
    void denyCoverRequestsByShiftId(int shiftId);

    List<CoverRequest> getCoverRequestByCovererId(int covererId);
}
