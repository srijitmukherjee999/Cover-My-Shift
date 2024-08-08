package com.techelevator.dao;

import com.techelevator.model.CoverRequest;

public interface CoverRequestDao {

    CoverRequest getCoverRequest(int shiftId, int employeeId);
    void createCoverRequest(int shiftId, int employeeId);
    void deleteCoverRequest(int shiftId, int employeeId);
    CoverRequest updateCoverRequest (CoverRequest coverRequest);
    void denyCoverRequestsByShiftId(int shiftId);
}
