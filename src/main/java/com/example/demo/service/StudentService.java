package com.example.demo.service;

import com.example.demo.model.LimitingStudentsException;
import com.example.demo.model.TypeClass;
import com.example.demo.model.Student;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService{
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Value("${max_student}")
    private Integer max_student;

    @Override
    public Student createStudent(Student student) throws Exception{
        if (classRoomRepository.getClassRoomById(student.getClass_id()).getType_class() == TypeClass.EXTERNAL &&
                classRoomRepository.getClassRoomById(student.getClass_id()).getNumber_of_students() >= max_student) {
            throw new LimitingStudentsException("An external class can have up to 20 students");
        }
        Student new_student =  studentRepository.createStudent(student);
        classRoomRepository.calcAvgAndStudentCount(classRoomRepository.getClassRoomById(student.getClass_id()),student.getClass_id());
        return new_student;
    }
    @Override
    public void updateStudent(Student student, Integer id) {
         studentRepository.updateStudent(student, id);
        classRoomRepository.calcAvgAndStudentCount(classRoomRepository.getClassRoomById(student.getClass_id()),student.getClass_id());
    }

    @Override
    public void deleteStudent(Integer id) {
        int save_id_class = studentRepository.getStudentById(id).getClass_id();
        studentRepository.deleteStudent(id);
        classRoomRepository.calcAvgAndStudentCount(classRoomRepository.getClassRoomById(save_id_class),save_id_class);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentRepository.getStudentById(id);
    }

    @Override
    public List<Student> getByExternalClass() {
        return studentRepository.getByExternalClass();
    }
}
