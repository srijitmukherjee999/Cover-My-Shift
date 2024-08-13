package com.techelevator.dao;

import com.techelevator.model.Shift;

import java.util.List;

public interface ShiftDao {

    Shift getShiftById(int shiftId);
    List<Shift> getAllShift();
    List<Shift> getShiftsByAssigned(int userId);
    void createShift(Shift shift);

    List<Shift> getShiftsByCoverer(int userId);

    Shift updateShift(Shift shift);

    void cleanupShift();
}
