package com.techelevator.dao;

import com.techelevator.model.UserShift;

public interface UserShiftDao {
    void createUserShift(int coverer, int shift_id);
    void deleteUserShiftBySh(int shift_id);
}
