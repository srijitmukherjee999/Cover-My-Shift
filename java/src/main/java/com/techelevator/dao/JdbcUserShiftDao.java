package com.techelevator.dao;


import com.techelevator.exception.DaoException;
import com.techelevator.model.UserShift;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcUserShiftDao implements UserShiftDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserShiftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUserShift(int coverer, int shift_id) {
        UserShift newUser = null;
        String insertUserSql = "INSERT INTO users_shift (shift_id, coverer) values (?, ?)";

        try {
            int newUserId = jdbcTemplate.update(insertUserSql, shift_id, coverer);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
}
