package com.techelevator.dao;

import com.techelevator.model.Vacation;

import java.security.Principal;
import java.util.List;

public interface VacationDao {
    Vacation getVacationById(int employeeId);
    List<Vacation> getListsOfVacation();
    Vacation createVacation(Vacation vacation, Principal principal);
    Vacation updateVacation(Vacation vacation);
    void deleteVacation(int id);

    Vacation getVacationByVacationId( int vacationID);
}
