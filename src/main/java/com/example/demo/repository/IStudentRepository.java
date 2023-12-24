package com.example.demo.repository;

import com.example.demo.model.NamedAlreadyExistException;
import com.example.demo.model.Student;

import java.util.List;

public interface IStudentRepository {
    Student createStudent(Student student) throws NamedAlreadyExistException;
    void updateStudent(Student student, Integer id);
    void deleteStudent(Integer id);
    List <Student> getAllStudents();
    Student getStudentById(Integer id);

    List<Student>getByExternalClass();
    List<Student>getByClassId(Integer class_id);
}
