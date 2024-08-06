package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.Shift;
import com.techelevator.model.User;
import com.techelevator.model.UserShift;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
public class EmployeeController {

    private final UserDao userDao;
    private final ShiftDao shiftDao;
    private final UserShiftDao userShiftDao;


    public EmployeeController(JdbcUserDao userDao, JdbcShiftDao shiftDao,JdbcUserShiftDao userShiftDao) {
        this.userDao = userDao;
        this.shiftDao = shiftDao;
        this.userShiftDao = userShiftDao;

    }

    @GetMapping(path = "/shifts")
    public List<Shift> getShifts(@RequestParam(required=false, defaultValue = "false") boolean mine, @RequestParam(required=false, defaultValue = "false") boolean emergency, @RequestParam(required = false, defaultValue = "0") int status, Principal principal){
        User user = userDao.getUserByUsername(principal.getName());
        List<Shift> shifts = shiftDao.getAllShift();
        if(mine) { // if filtering by mine...
            shifts.removeIf(s -> s.getCoverer() != user.getId()); // if coverer is not me, remove
        }
        if(emergency) { // if filtering by emergency...
            shifts.removeIf(s -> !s.isEmergency()); // if shift is not emergency, remove
        }
        if(status > 0) { // if filtering by status...
            shifts.removeIf(s -> s.getStatus() != status); // if status is not specified status, remove
        }
        return shifts;
    }

    @GetMapping(path = "/shift/{id}")
    public Shift getShift(@PathVariable int id){
        return shiftDao.getShiftById(id);
    }

    @PostMapping(path = "shift/{id}")
    public void createShift(@PathVariable int shiftId, Principal principal){
        int userId  = userDao.getUserByUsername(principal.getName()).getId();
        List<Shift> shifts = shiftDao.getAllShift();
        for(Shift shift : shifts) {
            if(shift.getStatus() == 3) {
                userShiftDao.createUserShift(userId, shiftId);
            }
        }
    }

    @PutMapping(path = "shift/{id}")
    public void updateShiftStatus(@RequestParam(required = true, defaultValue = "0") int status, @PathVariable int shiftId){
        Shift shift = shiftDao.getShiftById(shiftId);
        if(shift.getStatus() == 1 || shift.getStatus() == 3){
            shift.setStatus(2);
        }
        if(shift.getStatus() == 2) {
            shift.setStatus(3);
        }
    }

    @DeleteMapping(path = "shift/{id}")
    public void deleteUserShift(@PathVariable int shiftId){
        //confused


    }


}
