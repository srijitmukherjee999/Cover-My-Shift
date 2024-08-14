package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@CrossOrigin
@RestController
//@PreAuthorize("isAuthenticated()")
public class ManagerController {

    private final UserDao userDao;
    private final ShiftDao shiftDao;
    private final CoverRequestDao coverRequestDao;
    private final VacationDao vacationDao;


    public ManagerController(JdbcUserDao userDao, JdbcShiftDao shiftDao, JdbcCoverRequestDao coverRequestDao, JdbcVacationDao vacationDao) {
        this.userDao = userDao;
        this.shiftDao = shiftDao;
        this.coverRequestDao = coverRequestDao;
        this.vacationDao = vacationDao;
    }

    @GetMapping(path = "/user/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userDao.getUserById(userId);
    }

    @GetMapping(path = "/user/username")
    public User getUserByUsername(Principal principal){
        return userDao.getUserByUsername(principal.getName());
    }


    @GetMapping(path = "/user/{userId}/hours")
    public int getHoursById(@PathVariable int userId, @RequestParam String week) {
        if(userDao.getUserById(userId) == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee with ID " + userId + " does not exist.");
        }
        if(week == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide a starting date for the week.");
        }

        LocalDateTime startOfWeek;
        LocalDateTime endOfWeek;

        // if no date can be parsed from the string, show bad request
        try {
            startOfWeek = LocalDate.parse(week).atTime(0, 0);
            endOfWeek = LocalDate.parse(week).plusDays(7).atTime(23, 59);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide a valid date formatted as [yyyy-mm-dd].");
        }

        int hoursWorked = 0;
        for(Shift shift : shiftDao.getShiftsByCoverer(userId)){ // iterate through all shifts this user is covering
            // if shift is between startOfWeek and endOfWeek, add duration to the sum
            if(shift.getStartDateTime().isAfter(startOfWeek) && shift.getStartDateTime().isBefore(endOfWeek)) {
                hoursWorked += shift.getDuration();
            }
        }
        return hoursWorked;
    }

    @GetMapping(path = "/users")
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @GetMapping(path = "/shift/{id}/cover")
    public List<User> getCoverRequestByShiftId(@PathVariable int id, @RequestParam(required = false, defaultValue = "1") int status){
        return userDao.getCoverRequestsByShift(id,status);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/shift")
    public void addShiftToList(@Valid @RequestBody Shift shift){
        if(shift.getDuration() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift duration must be greater than 0.");
        }

        LocalDateTime start = shift.getStartDateTime();
        for( Vacation v : vacationDao.getVacationsByEmployeeId(shift.getAssignedId())){
            // for every vacation: if it's not before the start date or after the end date, it's during a vacation
            if(!(start.isBefore(v.getStartDate().atTime(0, 0)) || start.isAfter(v.getEndDate().atTime(23, 59)))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift would take place during vacation id " + v.getVacationId());
            }
        }
        shiftDao.createShift(shift);
    }

    @PutMapping(path = "/vacation/{id}")
    public Vacation updateVacationStatus(@PathVariable int id, @RequestParam(required = false, defaultValue = "0") int status, @Valid @RequestBody(required = false) Vacation update){
        Vacation vacation = null;
        if(status == 2 || status == 3){ // if status is specified in the params, only update that
            vacation = vacationDao.getVacationByVacationId(id);
            vacation.setStatus(status);
        }
        else if (update != null){
            update.setVacationId(id);
            vacation = update;
        }
        return vacationDao.updateVacation(vacation);
    }

    @PutMapping(path = "/manage/shifts")
    public Shift approveOrDenyCoverRequest(@Valid @RequestBody ApprovalDto approval){
        CoverRequest coverRequest = coverRequestDao.getCoverRequest(approval.getShiftId(), approval.getEmployeeId());
        if(coverRequest == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift cover request for shift id " + approval.getShiftId() + " by employee id " +approval.getEmployeeId() + " not found!");
        Shift shift = shiftDao.getShiftById(approval.getShiftId());
        coverRequest.setStatus(approval.isApproved() ? 2 : 3); // status 2 for approved, status 3 for not
        coverRequest.setMessage(approval.getMessage());
        coverRequestDao.updateCoverRequest(coverRequest);
        if(approval.isApproved()) {
            shift.setCovererId(approval.getEmployeeId());
            shift.setAssignedId(approval.getEmployeeId());
            shift.setStatus(4);
            coverRequest.setStatus(2);
            coverRequestDao.denyCoverRequestsByShiftId(shift.getShiftId());
            shift = shiftDao.updateShift(shift);
        }
        coverRequest.setStatus(3);
        return shift;
    }


    @PutMapping(path = "/shifts/{id}")
    public Shift updateShiftStatus(@RequestParam(defaultValue = "0") int status, @PathVariable int id){
        Shift shift = shiftDao.getShiftById(id);
        if (shift == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift not found.");
        }

        if(status > 0) {
            if(status == 3 && shift.getStatus() == 2){
                shift.setStatus(3);
                shift.setCovererId(0);
            }
            else if(status == 1 && shift.getStatus() == 2) {
                shift.setStatus(1);
                shift.setCovererId(shift.getAssignedId());
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal status change.");
            }
        }

        return shiftDao.updateShift(shift);
    }
}
