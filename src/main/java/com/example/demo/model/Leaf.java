package com.example.demo.model;

public class Leaf extends Component {

    public Leaf(Student student) {
        super(student);
    }

    @Override
    public void addStudent(Component component) {
        throw new IllegalStateException("leaf cannot execute this function");
    }

    @Override
    public void removeStudent(Component component) {
        throw new IllegalStateException("leaf cannot execute this function");
    }

    @Override
    public Float getAvg() {
        return student.getAvg_grade();

    }
}
