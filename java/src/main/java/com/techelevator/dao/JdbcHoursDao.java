package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Hours;
import com.techelevator.model.User;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class JdbcHoursDao implements HoursDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcHoursDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Hours getHoursByHoursId(int hoursId) {
        Hours hours = null;
        String sql = "SELECT hours_id, employee, week_year, hours_worked FROM hours WHERE hours_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, hoursId);
            if (results.next()) {
                hours = mapRowToHours(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return hours;
    }

    @Override
    public Hours getHoursByEmployee(int employee) {
        Hours hours = null;
        String sql = "SELECT hours_id, employee, week_year, hours_worked FROM hours WHERE employee = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, employee);
            if (results.next()) {
                hours = mapRowToHours(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return hours;
    }

    @Override
    public Hours getHoursByEmployeeWeek(int employee, int weekYear) {
        Hours hours = null;
        String sql = "SELECT hours_id, employee, week_year, hours_worked FROM hours WHERE employee = ? AND week_year = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, employee, weekYear);
            if (results.next()) {
                hours = mapRowToHours(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return hours;
    }

    private Hours mapRowToHours(SqlRowSet rs) {
        Hours hours = new Hours();
        hours.setHours_id(rs.getInt("hours_id"));
        hours.setEmployee(rs.getInt("employee"));
        hours.setWeekYear(rs.getInt("week_year"));
        hours.setHoursWorked(rs.getInt("hours_worked"));
        return hours;
    }
}
