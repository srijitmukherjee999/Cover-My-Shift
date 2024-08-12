package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.exception.DaoException;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
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
    public User getNameByAssignedId(@PathVariable int userId) {
        return userDao.getUserById(userId);
    }

    @GetMapping(path = "/users")
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @GetMapping(path = "/shift/{id}/cover")
    public List<User> getCoverRequestByShiftId(@PathVariable int id, @RequestParam(required = true) int status){
        return userDao.getCoverRequestsByShift(id,status);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/shift")
    public void addShiftToList(@Valid @RequestBody Shift shift){
        shiftDao.createShift(shift);
    }

    @GetMapping(path= "/vacations")
    public List<Vacation> getVacations(){
        return vacationDao.getVacations();
    }

    @PutMapping(path = "/vacation/{id}")
    public Vacation updateVacationStatus(@PathVariable int id, @Valid @RequestBody Vacation update){
        update.setVacationId(id);
        try{
            return vacationDao.updateVacation(update);
        }catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacation #" + id + "not found!!!");
        }
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
    public Shift updateShiftStatus(@RequestParam(required = false, defaultValue = "0") int status, @RequestParam(required = false, defaultValue = "false") boolean emergency, @PathVariable int id, Principal principal){
        Shift shift = shiftDao.getShiftById(id);
        User user = userDao.getUserByUsername(principal.getName());
        if (shift == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift not found.");
        }


        if (emergency){
            if(shift.getStartDateTime().isBefore(LocalDateTime.now().plusDays(1))) { // if before 1 day from now (aka within 24 hours)
                shift.setStatus(3);
                shift.setEmergency(true);
                shift.setCovererId(0);
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not schedule an emergency more than 24 hours out.");
            }
        }
        else if(status > 0) {
            if(status == 3 && shift.getStatus() == 2){
                shift.setStatus(3);
                shift.setCovererId(0);
            }
            else if(status == 1 && shift.getStatus() == 2) { // TODO: ask tom if approved days off can be canceled
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
