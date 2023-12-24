package com.example.demo.model;


public abstract class Component{
    protected  Student student;

    public Component(Student student) {
        this.student = student;
    }

    public abstract void addStudent(Component component);

    public abstract void removeStudent(Component component);
    public abstract Float getAvg();
}
