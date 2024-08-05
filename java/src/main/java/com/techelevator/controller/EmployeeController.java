package com.techelevator.controller;

import com.techelevator.dao.ShiftDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Shift;
import com.techelevator.model.User;
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


    public EmployeeController(UserDao userDao, ShiftDao shiftDao) {
        this.userDao = userDao;
        this.shiftDao = shiftDao;
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
}
