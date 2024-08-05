package com.techelevator.dao;

import com.techelevator.model.Hours;

public interface HoursDao {
    Hours getHoursByHoursId(int hoursId);
    Hours getHoursByEmployee(int employee);
    Hours getHoursByEmployeeWeek(int employee, int week_year);

}
