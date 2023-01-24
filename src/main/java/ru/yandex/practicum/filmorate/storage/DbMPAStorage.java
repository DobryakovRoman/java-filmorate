package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DbMPAStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbMPAStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA getMPA(int id) {
        String sqlQuery = "SELECT * FROM RATINGS WHERE MPA_ID = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRow.next()) {
            return new MPA(mpaRow.getInt("mpa_id"),
                    mpaRow.getString("name"));
        } else {
            throw new NotFoundException("MPA с id " + id + " не найден.");
        }
    }

    @Override
    public List<MPA> getAllMPAs() {
        List<MPA> allMpa = new ArrayList<>();
        String sqlQuery = "SELECT * FROM RATINGS";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery);
        while (mpaRow.next()) {
            allMpa.add(new MPA(mpaRow.getInt("mpa_id"),
                    mpaRow.getString("name")));
        }
        return allMpa;
    }
}
