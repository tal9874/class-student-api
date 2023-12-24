package com.example.demo.repository;

import com.example.demo.model.NamedAlreadyExistException;
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
public class StudentRepository implements IStudentRepository{
    protected static final String STUDENT_TABLE_NAME = "students";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    @Override
    public Student createStudent(Student student) throws NamedAlreadyExistException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String queryNamedParam = String.format("INSERT INTO %s (first_name, last_name, avg_grade, gender, class_id) VALUES (:first_name, :last_name, :avg_grade, :gender, :class_id)",STUDENT_TABLE_NAME);

            Map<String, Object> params = new HashMap<>();
            params.put("first_name", student.getFirst_name());
            params.put("last_name", student.getLast_name());
            params.put("avg_grade", student.getAvg_grade());
            params.put("gender", student.getGender().name());
            params.put("class_id", student.getClass_id());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Integer id = (Integer)generatedKeyHolder.getKeys().get("id");

            student.setId(id);

            return student;
        }
        catch (Exception e) {
            throw new NamedAlreadyExistException(String.format("student %s %s already exist" + student.getLast_name(), student.getFirst_name()));

        }
    }

    @Override
    public void updateStudent(Student student, Integer id) {
        String query = String.format("UPDATE %s SET first_name=?, last_name=?, avg_grade=?, gender = ? WHERE id= ?", STUDENT_TABLE_NAME);
        jdbcTemplate.update(query, student.getFirst_name(), student.getLast_name(), student.getAvg_grade(),
                student.getGender().name(), id);
    }

    @Override
    public void deleteStudent(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", STUDENT_TABLE_NAME);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Student> getAllStudents() {
        String query = String.format("Select * from %s", STUDENT_TABLE_NAME);
        return jdbcTemplate.query(query, new StudentMapper());
    }

    @Override
    public Student getStudentById(Integer id) {
        String query = String.format("Select * from %s where id=?", STUDENT_TABLE_NAME);
        try
        {
            return jdbcTemplate.queryForObject(query, new StudentMapper(), id);
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
                ");",STUDENT_TABLE_NAME,ClassRoomRepository.CLASSROOM_TABLE_NAME);
        return  jdbcTemplate.query(query,new StudentMapper());
    }

    public List<Student> getByClassId(Integer class_id) {
        String query = String.format("Select * FROM %s WHERE class_id = ?",StudentRepository.STUDENT_TABLE_NAME);
        return jdbcTemplate.query(query,new StudentMapper(),class_id);
    }
}
