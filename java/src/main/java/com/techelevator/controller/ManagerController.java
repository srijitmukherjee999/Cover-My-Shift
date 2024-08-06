package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.Shift;
import com.techelevator.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
public class ManagerController {

    private final UserDao userDao;
    private final ShiftDao shiftDao;
    private final UserShiftDao userShiftDao;


    public ManagerController(JdbcUserDao userDao, JdbcShiftDao shiftDao, JdbcUserShiftDao userShiftDao) {
        this.userDao = userDao;
        this.shiftDao = shiftDao;
        this.userShiftDao = userShiftDao;

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

    @PostMapping(path = "/shift")
    public void addShiftToList(@Valid @RequestBody Shift shift){
        shiftDao.createShift(shift);
    }
}
