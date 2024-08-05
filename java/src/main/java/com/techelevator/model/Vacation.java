package com.techelevator.model;

import java.time.LocalDate;

public class Vacation {
    private int vacationId;
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int status;

    public Vacation (){}

    public Vacation(int vacationId, int employeeId, LocalDate startDate, LocalDate endDate, int status) {
        this.vacationId = vacationId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
