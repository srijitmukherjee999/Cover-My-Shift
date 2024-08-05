package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Vacation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcVacationDao implements VacationDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcVacationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public Vacation mapRowToVacation(SqlRowSet rowSet){
        Vacation vacation = new Vacation();
        vacation.setVacationId(rowSet.getInt("vacation_id"));
        vacation.setEmployeeId(rowSet.getInt("employee"));
        vacation.setStartDate(rowSet.getDate("start_date").toLocalDate());
        vacation.setEndDate(rowSet.getDate("end_date").toLocalDate());
        vacation.setStatus(rowSet.getInt("status"));

        return vacation;
    }
}
