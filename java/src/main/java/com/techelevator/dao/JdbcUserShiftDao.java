package com.techelevator.dao;


import com.techelevator.exception.DaoException;
import com.techelevator.model.UserShift;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcUserShiftDao implements UserShiftDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserShiftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUserShift(int coverer, int shift_id) {

        String insertUserSql = "INSERT INTO users_shift (shift_id, coverer) values (?, ?)";

        try {
            jdbcTemplate.update(insertUserSql, shift_id, coverer);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteUserShiftBySh(int shift_id){
        String insertUserSql = "DELETE FROM users_shift WHERE shift_id = ?";

        try {
            jdbcTemplate.update(insertUserSql, shift_id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
}
