package com.techelevator.dao;

import com.techelevator.model.UserShift;

public interface UserShiftDao {

    UserShift getUserShift(int shiftId, int employeeId);
    void createUserShift(int shiftId, int employeeId);
    void deleteUserShift(int shiftId, int employeeId);
}
