package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Shift;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcShiftDao implements ShiftDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcShiftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Shift getShiftById(int shiftId) {

        Shift shift = null;
        String sql = "SELECT * FROM shift" +
                " WHERE shift_id = ?;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,shiftId);
            if(result.next()){
                shift = mapRowToShift(result);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return shift;
    }

    @Override
    public List<Shift> getAllShift() {

        List<Shift> shiftList = new ArrayList<>();
        String sql = "SELECT * FROM shift;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                Shift shifts = mapRowToShift(results);
                shiftList.add(shifts);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return shiftList;
    }

    @Override
    public List<Shift> getShiftsByUser(int userId) {

        List<Shift> shifts = new ArrayList<>();
        String sql = "SELECT * FROM shift" +
                " WHERE assigned = ?;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userId);
            while(result.next()){
                shifts.add(mapRowToShift(result));
            }

        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return shifts;
    }

    @Override
    public Shift getShiftByEmergency(boolean emergency) {

        Shift shift = new Shift();
        String sql = "SELECT * FROM shift" +
                " WHERE emergency = ?;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,emergency);
            if(result.next()){
                shift = mapRowToShift(result);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return shift;

    }

    @Override
    public Shift updateShift(Shift shift) {
        String sql = "UPDATE shift SET assigned = ?, start_date_time = ?, duration = ?, status = ?, emergency = ?, coverer = ?, description = ? WHERE shift_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, shift.getAssigned(), shift.getStartDateTime(), shift.getDuration(), shift.getStatus(), shift.isEmergency(), shift.getCoverer(), shift.getDescription(), shift.getShiftId());

            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                return getShiftById(shift.getShiftId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void createShift(Shift shift) {

        String insertUserSql = "INSERT INTO shift (assigned, start_date_time, duration, status, emergency, coverer, description) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(insertUserSql,shift.getAssigned(), shift.getStartDateTime(), shift.getDuration(), shift.getStatus(), shift.getCoverer(), shift.getDescription());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    /*if(rowSet.getDate("start_date_time") !=null) {
        shift.setStartDateTime(rowSet.getTimestamp("start_date_time").toLocalDateTime());
        }*/

    public Shift mapRowToShift(SqlRowSet rowSet){

        Shift shift = new Shift();
        shift.setShiftId(rowSet.getInt("shift_id"));
        shift.setAssigned(rowSet.getInt("assigned"));
        shift.setStartDateTime(rowSet.getTimestamp("start_date_time").toLocalDateTime());
        shift.setDuration(rowSet.getInt("duration"));
        shift.setStatus(rowSet.getInt("status"));
        shift.setEmergency(rowSet.getBoolean("emergency"));
        shift.setCoverer(rowSet.getInt("coverer"));
        shift.setDescription(rowSet.getString("description"));

        return shift;
    }
}
