package com.techelevator.model;

public class CoverRequest {
    private int shiftId;
    private int covererId;
    private int status;
    private String message;

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getCovererId() {
        return covererId;
    }

    public void setCovererId(int covererId) {
        this.covererId = covererId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CoverRequest{" +
                "shiftId=" + shiftId +
                ", covererId=" + covererId +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
