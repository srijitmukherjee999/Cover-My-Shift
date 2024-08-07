package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Vacation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcVacationDao implements VacationDao{

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public JdbcVacationDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public Vacation getVacationById(int employeeId) {
        Vacation vacation = new Vacation();
        String sql = "SELECT * FROM vacation" +
                " WHERE employee = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, employeeId);
            if (result.next()) {
                vacation = mapRowToVacation(result);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return vacation;
    }

    @Override
    public List<Vacation> getListsOfVacation() {
        List<Vacation> vacationList = new ArrayList<>();
        String sql = "SELECT * FROM vacation;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
            while(result.next()){
                Vacation vacation = mapRowToVacation(result);
                vacationList.add(vacation);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return vacationList;
    }

    @Override
    public Vacation createVacation(Vacation vacation, Principal principal) {
        int id = userDao.getUserByUsername(principal.getName()).getId();

        String sql = "INSERT INTO vacation (employee, start_date, end_date, status, description)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING vacation_id ;";
        try {

            int newVacationId = jdbcTemplate.queryForObject(sql, int.class, id,
                    vacation.getStartDate(), vacation.getEndDate(), 1 ,vacation.getDescription());
            return getVacationByVacationId(newVacationId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public Vacation updateVacation(Vacation vacation) {

        String sql = "UPDATE vacation SET employee = ?, start_date = ?, end_date= ?, status = ?" +
                " WHERE vacation_id = ?";

        try {
            int rowAffected = jdbcTemplate.update(sql, vacation.getEmployeeId(), vacation.getStartDate(), vacation.getEndDate(), vacation.getStatus(), vacation.getVacationId());
            if (rowAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                return getVacationById(vacation.getVacationId());
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteVacation(int id) {

        String deleteVacation = "DELETE FROM vacation WHERE vacation_id = ?;";

        try {
            jdbcTemplate.update(deleteVacation, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public Vacation getVacationByVacationId(int vacationID) {
        String sql = "SELECT * FROM vacation WHERE vacation_id = ?;" ;
        Vacation vac = null;

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,vacationID);
        while(results.next()){
            vac = mapRowToVacation(results);
        }
        return vac;
    }

    public Vacation mapRowToVacation(SqlRowSet rowSet){
        Vacation vacation = new Vacation();
        vacation.setVacationId(rowSet.getInt("vacation_id"));
        vacation.setEmployeeId(rowSet.getInt("employee"));
        vacation.setStartDate(rowSet.getDate("start_date").toLocalDate());
        vacation.setEndDate(rowSet.getDate("end_date").toLocalDate());
        vacation.setStatus(rowSet.getInt("status"));
        vacation.setDescription(rowSet.getString("description"));

        return vacation;
    }
}
