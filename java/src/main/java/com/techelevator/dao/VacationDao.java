package com.techelevator.dao;

import com.techelevator.model.Vacation;

import java.util.List;

public interface VacationDao {
    Vacation getVacationById(int employeeId);
    List<Vacation> getListsOfVacation();
    Vacation createVacation(Vacation vacation);
    Vacation updateVacation(Vacation vacation);
}
