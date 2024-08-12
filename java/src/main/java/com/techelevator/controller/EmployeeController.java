package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.Shift;
import com.techelevator.model.User;
import com.techelevator.model.Vacation;
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
@PreAuthorize("isAuthenticated()")
public class EmployeeController {

    private final UserDao userDao;
    private final ShiftDao shiftDao;
    private final CoverRequestDao coverRequestDao;
    private final VacationDao vacationDao;


    public EmployeeController(JdbcUserDao userDao, JdbcShiftDao shiftDao, JdbcCoverRequestDao coverRequestDao, JdbcVacationDao vacationDao) {
        this.userDao = userDao;
        this.shiftDao = shiftDao;
        this.coverRequestDao = coverRequestDao;
        this.vacationDao = vacationDao;
    }
    @GetMapping(path = "/shifts")
    public List<Shift> getShifts(@RequestParam(required=false, defaultValue = "false") boolean mine, @RequestParam(required=false, defaultValue = "false") boolean emergency, @RequestParam(required = false, defaultValue = "0") int status, @RequestParam(required = false, defaultValue = "false") boolean assigned, Principal principal){
        User user = userDao.getUserByUsername(principal.getName());
        List<Shift> shifts = shiftDao.getAllShift();
        if(mine) { // if filtering by mine...
            shifts.removeIf(s -> s.getCovererId() != user.getId()); // if coverer is not me, remove
        }
        if(emergency) { // if filtering by emergency...
            shifts.removeIf(s -> !s.isEmergency()); // if shift is not emergency, remove
        }
        if(status > 0) { // if filtering by status...
            shifts.removeIf(s -> s.getStatus() != status); // if status is not specified status, remove
        }
        if(assigned) { // if filtering by assigned...
            shifts.removeIf(s -> s.getAssignedId() != user.getId()); // if assigned is not me, remove
        }
        return shifts;
    }
    @GetMapping(path = "/shift/{id}")
    public Shift getShift(@PathVariable int id){
        return shiftDao.getShiftById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/shift/{id}")
    public void createShiftRequest(@PathVariable int id, Principal principal){
        int userId  = userDao.getUserByUsername(principal.getName()).getId();
        Shift shift = shiftDao.getShiftById(id);
        if(shift == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift not found.");
        }

        if(shift.getStatus() == 3) {
            coverRequestDao.createCoverRequest(id, userId);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This shift is not taking cover requests.");
        }
    }

    @PutMapping(path = "shift/{id}")
    public Shift updateShiftStatus(@RequestParam(required = false, defaultValue = "0") int status, @RequestParam(required = false, defaultValue = "false") boolean emergency, @PathVariable int id, Principal principal){
        Shift shift = shiftDao.getShiftById(id);
        User user = userDao.getUserByUsername(principal.getName());
        if (shift == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift not found.");
        }
        if (shift.getAssignedId() != user.getId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not assigned to this shift.");
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
            if(status == 2 && shift.getStatus() == 1){
                shift.setStatus(2);
                shift.setCovererId(0);
            }
            else if(status == 1 && shift.getStatus() == 2) { // TODO: ask tom if approved days off can be canceled
                shift.setStatus(1);
                shift.setCovererId(user.getId());
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal status change.");
            }
        }

        return shiftDao.updateShift(shift);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/shift/{id}")
    public void deleteCoverRequest(@PathVariable int id, Principal principal){
        coverRequestDao.deleteCoverRequest(id, userDao.getUserByUsername(principal.getName()).getId());
    }


    @GetMapping(path = "/shifts/username")
    public String getUserFullName(Principal principal){
       return  userDao.getUserByUsername(principal.getName()).getFullName();
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/vacations")
    public Vacation createVacationRequest(@Valid @RequestBody Vacation vacation){
         return vacationDao.createVacation(vacation);
    }

    @GetMapping(path = "/vacations")
    public List<Vacation> getVacations(){
        return vacationDao.getVacations();
    }

    @GetMapping(path = "/vacations/{id}")
    public List<Vacation> getVacationById(@PathVariable int id){
        return vacationDao.getVacationsByEmployeeId(id);
    }

//    @GetMapping(path = "/user/fullName")
//    public User getFullNameFromLoggedInUser(Principal principal) {
//        return userDao.getUserByUsername(principal.getName());
//    }
}
