package com.techelevator.model;
/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, LoginDto represents the data a
    client must pass to the server for a login endpoint, and LoginResponseDto represents the object that's returned
    from the server to the client from a login endpoint.
 */
public class ApprovalDto {

   private int shiftId;
   private int employeeId;
   private boolean approved;
   private String message;

   public int getShiftId() {
      return shiftId;
   }

   public void setShiftId(int shiftId) {
      this.shiftId = shiftId;
   }

   public int getEmployeeId() {
      return employeeId;
   }

   public void setEmployeeId(int employeeId) {
      this.employeeId = employeeId;
   }

   public boolean isApproved() {
      return approved;
   }

   public void setApproved(boolean approved) {
      this.approved = approved;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   @Override
   public String toString() {
      return "ApprovalDto{" +
              "shiftId=" + shiftId +
              ", employeeId=" + employeeId +
              ", approved=" + approved +
              ", message='" + message + '\'' +
              '}';
   }
}
