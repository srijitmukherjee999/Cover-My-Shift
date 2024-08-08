package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.exception.DaoException;
import com.techelevator.model.ApprovalDto;
import com.techelevator.model.Shift;
import com.techelevator.model.User;
import com.techelevator.model.Vacation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
public class ManagerController {

    private final UserDao userDao;
    private final ShiftDao shiftDao;
    private final UserShiftDao userShiftDao;
    private final VacationDao vacationDao;


    public ManagerController(JdbcUserDao userDao, JdbcShiftDao shiftDao, JdbcUserShiftDao userShiftDao, JdbcVacationDao vacationDao) {
        this.userDao = userDao;
        this.shiftDao = shiftDao;
        this.userShiftDao = userShiftDao;
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
    public List<User> getCoverRequestByShiftId(@PathVariable int id){
        return userDao.getCoverRequestsByShift(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/shift")
    public void addShiftToList(@Valid @RequestBody Shift shift){
        shiftDao.createShift(shift);
    }

    @GetMapping(path= "/vacations")
    public List<Vacation> getVacations(){
        return vacationDao.getListsOfVacation();
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
        if(userShiftDao.getUserShift(approval.getShiftId(), approval.getEmployeeId()) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift cover request for shift id " + approval.getShiftId() + " by employee id " +approval.getEmployeeId() + " not found!");
        Shift shift = shiftDao.getShiftById(approval.getShiftId());
        if(approval.isApproved()) {
            shift.setCovererId(approval.getEmployeeId());
            shift.setStatus(4);
            shift = shiftDao.updateShift(shift);
        }
        userShiftDao.deleteUserShift(approval.getShiftId(), approval.getEmployeeId());
        return shift;
    }
}
