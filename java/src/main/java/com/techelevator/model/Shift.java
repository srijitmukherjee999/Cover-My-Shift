package com.techelevator.model;

import java.time.LocalDateTime;

public class Shift {
    private int shiftId;
    private int assignedId;
    private String assignedName;
    private LocalDateTime startDateTime;
    private int duration;
    private int status;
    private boolean emergency;
    private int covererId;
    private String covererName;
    private String description;

    public Shift (){}
    public Shift(int shiftId, int assignedId, String assignedName, LocalDateTime startDateTime, int duration, int status, boolean emergency, int covererId, String covererName, String description) {
        this.shiftId = shiftId;
        this.assignedId = assignedId;
        this.assignedName = assignedName;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.status = status;
        this.emergency = emergency;
        this.covererId = covererId;
        this.covererName = covererName;
        this.description = description;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(int assignedId) {
        this.assignedId = assignedId;
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

    public int getCovererId() {
        return covererId;
    }

    public void setCovererId(int covererId) {
        this.covererId = covererId;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public void setAssignedName(String assignedName) {
        this.assignedName = assignedName;
    }

    public String getCovererName() {
        return covererName;
    }

    public void setCovererName(String covererName) {
        this.covererName = covererName;
    }
}
