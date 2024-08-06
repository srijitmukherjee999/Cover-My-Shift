package com.techelevator.dao;

import com.techelevator.model.UserShift;

public interface UserShiftDao {
    void createUserShift(int coverer, int shift_id);
    void deleteUserShift(int shift_id, int employee_id);
}
