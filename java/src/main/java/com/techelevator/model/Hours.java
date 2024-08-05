package com.techelevator.model;

public class Hours {
    private int hours_id;
    private int employee;
    private int weekYear;
    private int hoursWorked;

    public Hours(int hours_id, int employee, int weekYear, int hoursWorked) {
        this.hours_id = hours_id;
        this.employee = employee;
        this.weekYear = weekYear;
        this.hoursWorked = hoursWorked;
    }

    public int getHours_id() {
        return hours_id;
    }

    public void setHours_id(int hours_id) {
        this.hours_id = hours_id;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getWeekYear() {
        return weekYear;
    }

    public void setWeekYear(int weekYear) {
        this.weekYear = weekYear;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
