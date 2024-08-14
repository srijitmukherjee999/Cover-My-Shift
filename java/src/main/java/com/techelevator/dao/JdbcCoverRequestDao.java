package com.techelevator.dao;


import com.techelevator.exception.DaoException;
import com.techelevator.model.CoverRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCoverRequestDao implements CoverRequestDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCoverRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CoverRequest getCoverRequest(int shiftId, int employeeId){
        CoverRequest coverRequest = null;
        String sql = "SELECT shift_id, coverer_id, status, message FROM cover_request WHERE shift_id = ? AND coverer_id = ?";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, shiftId, employeeId);
            if(result.next()){
                coverRequest = mapRowToCoverRequest(result);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return coverRequest;
    }

    @Override
    public void createCoverRequest(int shiftId, int employeeId) {

        String insertUserSql = "INSERT INTO cover_request (shift_id, coverer_id,status) values (?, ?,?)";

        try {
            jdbcTemplate.update(insertUserSql, shiftId, employeeId,1);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteCoverRequest(int shiftId, int employeeId){
        String insertUserSql = "DELETE FROM cover_request WHERE shift_id = ? AND coverer_id = ?";

        try {
            jdbcTemplate.update(insertUserSql, shiftId, employeeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public CoverRequest updateCoverRequest(CoverRequest coverRequest){
        String sql = "UPDATE cover_request SET status = ?, message = ? WHERE shift_id = ? AND coverer_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, coverRequest.getStatus(), coverRequest.getMessage(), coverRequest.getShiftId(), coverRequest.getCovererId());

            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                return getCoverRequest(coverRequest.getShiftId(), coverRequest.getCovererId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void denyCoverRequestsByShiftId(int shiftId){ // denies all *pending* requests on a shift (use when accepting a cover request)
        String sql = "UPDATE cover_request SET status = 3, message = 'Another cover request was accepted.' WHERE shift_id = ? AND status = 1";
        try {
            jdbcTemplate.update(sql, shiftId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<CoverRequest> getCoverRequestByCovererId(int covererId) {

        List<CoverRequest> listOfCoverRequests = new ArrayList<>();

        String sql = "SELECT * FROM cover_request WHERE coverer_id = ?";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,covererId);
            while(result.next()){
                listOfCoverRequests.add(mapRowToCoverRequest(result)) ;
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return listOfCoverRequests;
    }


    private CoverRequest mapRowToCoverRequest(SqlRowSet rowSet){

        CoverRequest coverRequest = new CoverRequest();
        coverRequest.setShiftId(rowSet.getInt("shift_id"));
        coverRequest.setCovererId(rowSet.getInt("coverer_id"));
        coverRequest.setStatus(rowSet.getInt("status"));
        coverRequest.setMessage(rowSet.getString("message"));

        return coverRequest;
    }
}
