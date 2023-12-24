package com.example.demo.model;

import java.util.ArrayList;


public class Composite extends Component {


    private ArrayList <Component> componentArrayList = new ArrayList<>();

    public Composite(Student student) {
        super(student);
    }

    public void addStudent(Component component) {
        componentArrayList.add(component);
    }
    public void removeStudent(Component component) {
        componentArrayList.remove(component);
    }

    @Override
    public Float getAvg() {
        Float sum = 0.0f;
        for (Component component:componentArrayList) {
            sum += component.getAvg();
        }
        return sum / componentArrayList.size();
    }
}
