package com.techelevator.dao;


import com.techelevator.exception.DaoException;
import com.techelevator.model.UserShift;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcUserShiftDao implements UserShiftDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserShiftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserShift getUserShift(int shiftId, int employeeId){
        UserShift userShift = null;
        String sql = "SELECT shift_id, coverer_id FROM users_shift WHERE shift_id = ? AND coverer_id = ?";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, shiftId, employeeId);
            if(result.next()){
                userShift = mapRowToUserShift(result);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userShift;
    }

    @Override
    public void createUserShift(int shiftId, int employeeId) {

        String insertUserSql = "INSERT INTO users_shift (shift_id, coverer_id) values (?, ?)";

        try {
            jdbcTemplate.update(insertUserSql, shiftId, employeeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteUserShift(int shiftId, int employeeId){
        String insertUserSql = "DELETE FROM users_shift WHERE shift_id = ? AND coverer_id = ?";

        try {
            jdbcTemplate.update(insertUserSql, shiftId, employeeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private UserShift mapRowToUserShift(SqlRowSet rowSet){

        UserShift userShift = new UserShift();
        userShift.setShiftId(rowSet.getInt("shift_id"));
        userShift.setCoverer(rowSet.getInt("coverer_id"));

        return userShift;
    }
}
