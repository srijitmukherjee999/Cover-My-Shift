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

    private final String SQL_SELECT_TEMPLATE = "SELECT shift_id, assigned, assign.full_name AS assigned_name, start_date_time, duration, status, emergency, coverer, cover.full_name AS coverer_name, description" +
            " FROM shift" +
            " JOIN users assign ON assign.user_id = shift.assigned" +
            " LEFT OUTER JOIN users cover ON cover.user_id = shift.coverer";

    public JdbcShiftDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Shift getShiftById(int shiftId) {

        Shift shift = null;
        String sql = SQL_SELECT_TEMPLATE + " WHERE shift_id = ?";
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
        String sql = SQL_SELECT_TEMPLATE;
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                Shift shifts = mapRowToShift(results);
                shiftList.add(shifts);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return shiftList;
    }

    @Override
    public List<Shift> getShiftsByUser(int userId) {

        List<Shift> shifts = new ArrayList<>();
        String sql = SQL_SELECT_TEMPLATE +
                " WHERE assigned = ?;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userId);
            while(result.next()){
                shifts.add(mapRowToShift(result));
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return shifts;
    }

    @Override
    public Shift updateShift(Shift shift) {
        String sql = "UPDATE shift SET assigned = ?, start_date_time = ?, duration = ?, status = ?, emergency = ?, coverer = ?, description = ? WHERE shift_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, shift.getAssignedId(), shift.getStartDateTime(), shift.getDuration(), shift.getStatus(), shift.isEmergency(), (shift.getCovererId() > 0 ? shift.getCovererId() : null), shift.getDescription(), shift.getShiftId());

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

        String insertShiftSql = "INSERT INTO shift (assigned, start_date_time, duration, status, emergency, coverer, description) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING shift_id";

        try {
            jdbcTemplate.queryForObject(
                    insertShiftSql,
                    Integer.class,
                    shift.getAssignedId(),
                    shift.getStartDateTime(),
                    shift.getDuration(),
                    shift.getStatus(),
                    shift.isEmergency(),
                    shift.getCovererId(),
                    shift.getDescription()
            );

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
        shift.setAssignedId(rowSet.getInt("assigned"));
        shift.setAssignedName(rowSet.getString("assigned_name"));
        shift.setStartDateTime(rowSet.getTimestamp("start_date_time").toLocalDateTime());
        shift.setDuration(rowSet.getInt("duration"));
        shift.setStatus(rowSet.getInt("status"));
        shift.setEmergency(rowSet.getBoolean("emergency"));
        shift.setCovererId(rowSet.getInt("coverer"));
        shift.setCovererName(rowSet.getString("coverer_name"));
        shift.setDescription(rowSet.getString("description"));

        return shift;
    }
}
