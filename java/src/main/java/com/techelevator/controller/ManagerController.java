package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public User getNameByAssignedId(@PathVariable int id) {
        return userDao.getUserById(id);
    }
}
