package com.example.demo.repository;

import com.example.demo.model.ClassRoom;
import com.example.demo.model.ClassRoomMapper;
import com.example.demo.model.Student;
import com.example.demo.model.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository

public class ClassRoomRepository implements IClassRoomRepository{
    protected static final String CLASSROOM_TABLE_NAME = "classroom";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    @Override
    public ClassRoom createClassRoom(ClassRoom classRoom) throws Exception {
        try {
//            String query = String.format("INSERT INTO %s (number_of_students, class_avg, type_class) VALUES (?, ?, ?)", CLASSROOM_TABLE_NAME);
//            jdbcTemplate.update(query, classRoom.getNumber_of_students(), classRoom.getClass_avg(),
//                    classRoom.getType_class().name());
//            return null;
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String queryNamedParam = String.format("INSERT INTO %s (number_of_students, class_avg, type_class) VALUES (:number_of_students, :class_avg, :type_class)",CLASSROOM_TABLE_NAME);
            Map<String, Object> params = new HashMap<>();
            params.put("number_of_students", classRoom.getNumber_of_students());
            params.put("class_avg", classRoom.getClass_avg());
            params.put("type_class", classRoom.getType_class().name());
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Integer id = (Integer)generatedKeyHolder.getKeys().get("id");

            classRoom.setId(id);

            return classRoom;
        }
        catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    @Override
    public void updateClassRoom(ClassRoom classRoom, Integer id) {
        String query = String.format("UPDATE %s SET number_of_students=?, class_avg=?, type_class=? WHERE id= ?", CLASSROOM_TABLE_NAME);
        jdbcTemplate.update(query, classRoom.getNumber_of_students(), classRoom.getClass_avg(),
                classRoom.getType_class().name(),id);
    }

    @Override
    public void deleteClassRoom(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", CLASSROOM_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }

    @Override
    public void calcAvgAndStudentCount(ClassRoom classRoom,Integer id) {
        try {
            String query = String.format("UPDATE %s SET number_of_students = (SELECT COUNT(*) FROM %s WHERE class_id = %d), " +
                            "class_avg = CASE WHEN (SELECT COUNT(*) FROM %s WHERE class_id = %d) > 0 " +
                            "THEN (SELECT AVG(avg_grade) FROM %s WHERE class_id = %d) ELSE 0.0 END WHERE id = %d",
                    CLASSROOM_TABLE_NAME, StudentRepository.STUDENT_TABLE_NAME, id,
                    StudentRepository.STUDENT_TABLE_NAME, id,
                    StudentRepository.STUDENT_TABLE_NAME, id, id);
            jdbcTemplate.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<ClassRoom> getAllClassRooms() {
        String query = String.format("Select * from %s", CLASSROOM_TABLE_NAME);
        return jdbcTemplate.query(query, new ClassRoomMapper());
    }

    @Override
    public ClassRoom getClassRoomById(Integer id) {
        String query = String.format("Select * from %s where id=?", CLASSROOM_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new ClassRoomMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Student> getByExternalClass() {
        String query = String.format("SELECT *\n" +
                "FROM %s\n" +
                "WHERE class_id IN (\n" +
                "    SELECT id\n" +
                "    FROM %s\n" +
                "    WHERE type_class = 'EXTERNAL'\n" +
                ");",StudentRepository.STUDENT_TABLE_NAME,CLASSROOM_TABLE_NAME);
        return  jdbcTemplate.query(query,new StudentMapper());
    }

}
