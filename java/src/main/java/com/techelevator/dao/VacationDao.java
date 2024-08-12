package com.techelevator.dao;

import com.techelevator.model.Vacation;

import java.util.List;

public interface VacationDao {
    Vacation getVacationById(int employeeId);
    List<Vacation> getVacationsByEmployeeId(int employeeId);
    List<Vacation> getVacations();
    Vacation createVacation(Vacation vacation);
    Vacation updateVacation(Vacation vacation);
    void deleteVacation(int id);

    Vacation getVacationByVacationId( int vacationID);
}
