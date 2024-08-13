package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Vacation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcVacationDao implements VacationDao{

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    private final String SQL_SELECT_TEMPLATE = "SELECT vacation_id, employee, users.full_name AS employee_name, start_date, end_date, status, description" +
            " FROM vacation" +
            " JOIN users ON vacation.employee = users.user_id";

    public JdbcVacationDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public Vacation getVacationById(int id) {
        Vacation vacation = new Vacation();
        String sql = SQL_SELECT_TEMPLATE + " WHERE vacation_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
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
    public List<Vacation> getVacationsByEmployeeId(int employeeId){
        List<Vacation> vacations = new ArrayList<>();
        String sql = SQL_SELECT_TEMPLATE + " WHERE employee = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, employeeId);
            while(result.next()) {
                vacations.add(mapRowToVacation(result));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return vacations;
    }

    @Override
    public List<Vacation> getVacations() {
        List<Vacation> vacationList = new ArrayList<>();
        String sql = SQL_SELECT_TEMPLATE;
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
    public Vacation createVacation(Vacation vacation) {

        String sql = "INSERT INTO vacation (employee, start_date, end_date, status, description)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING vacation_id ;";
        try {

            int newVacationId = jdbcTemplate.queryForObject(sql, int.class, vacation.getEmployeeId(),
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
        String sql = SQL_SELECT_TEMPLATE + " WHERE vacation_id = ?;";
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
        vacation.setEmployeeName(rowSet.getString("employee_name"));
        vacation.setStartDate(rowSet.getDate("start_date").toLocalDate());
        vacation.setEndDate(rowSet.getDate("end_date").toLocalDate());
        vacation.setStatus(rowSet.getInt("status"));
        vacation.setDescription(rowSet.getString("description"));

        return vacation;
    }
}
