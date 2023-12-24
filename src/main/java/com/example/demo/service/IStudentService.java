package com.example.demo.service;

import com.example.demo.model.Student;

import java.util.List;


public interface IStudentService {
    Student createStudent(Student student) throws Exception;
    void updateStudent(Student student, Integer id);
    void deleteStudent(Integer id);
    List<Student> getAllStudents();
    Student getStudentById(Integer id);
    List<Student>getByExternalClass();
}
