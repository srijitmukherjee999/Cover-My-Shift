package com.techelevator.dao;

import com.techelevator.model.Vacation;

public interface VacationDao {
    Vacation getVacationById(int employeeId);
}
