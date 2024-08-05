package com.techelevator.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Shift {
    private int shiftId;
    private int assigned;
    private LocalDateTime startDateTime;
    private int duration;
    private int status;
    private boolean emergency;
    private int coverer;
    private String description;

    public Shift (){}
    public Shift(int shiftId, int assigned, LocalDateTime startDateTime, int duration, int status, boolean emergency, int coverer, String description) {
        this.shiftId = shiftId;
        this.assigned = assigned;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.status = status;
        this.emergency = emergency;
        this.coverer = coverer;
        this.description =  description;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getAssigned() {
        return assigned;
    }

    public void setAssigned(int assigned) {
        this.assigned = assigned;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public int getCoverer() {
        return coverer;
    }

    public void setCoverer(int coverer) {
        this.coverer = coverer;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
