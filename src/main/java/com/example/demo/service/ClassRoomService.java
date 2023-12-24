package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRoomService implements IClassRoomService{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Value("${min_of_student_class}")
    private Integer min_of_student_class;

    @Value("${max_student}")
    private Integer max_student;


    @Override
    public ClassRoom createClassRoom(ClassRoom classRoom) throws Exception {
       return classRoomRepository.createClassRoom(classRoom);
    }

    @Override
    public void updateClassRoom(ClassRoom classRoom, Integer id) {
        classRoomRepository.updateClassRoom(classRoom,id);
    }

    @Override
    public void deleteClassRoom(Integer id) throws Exception {
        if (classRoomRepository.getClassRoomById(id).getNumber_of_students() >= min_of_student_class){
            throw new ClassNotFoundException("You cannot delete a class that has students");
        }
        classRoomRepository.deleteClassRoom(id);
    }

    @Override
    public List<ClassRoom> getAllClassRooms() {
        List <Integer> result = studentRepository.getAllStudents().stream().map(student -> student.getClass_id()).distinct().toList();
        for (Integer id : result){
            classRoomRepository.calcAvgAndStudentCount(classRoomRepository.getClassRoomById(id),id);
        }
        return classRoomRepository.getAllClassRooms();
    }

    @Override
    public ClassRoom getClassRoomById(Integer id) {
        return classRoomRepository.getClassRoomById(id);
    }

    @Override
    public List<Student> getByExternalClass() {
        return classRoomRepository.getByExternalClass();
    }

    @Override
    public MyDTOResponse getMyDTO(Integer id) {
        try {
            if (classRoomRepository.getClassRoomById(id).getNumber_of_students() < min_of_student_class) {
                return new MyDTOResponse(classRoomRepository.getClassRoomById(id), null);
            } else {
                classRoomRepository.calcAvgAndStudentCount(classRoomRepository.getClassRoomById(id), id);
                return new MyDTOResponse(classRoomRepository.getClassRoomById(id), studentRepository.getByClassId(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MyDTOResponse CreateStudentGetDTO(Student newStudent) throws NamedAlreadyExistException {
             studentRepository.createStudent(newStudent);
             classRoomRepository.calcAvgAndStudentCount(classRoomRepository.getClassRoomById(newStudent.getClass_id()), newStudent.getClass_id());
            return getMyDTO(newStudent.getClass_id());

    }
}
