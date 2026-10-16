package ru.ranepa;

import java.util.List;

public class Student {
    public String name;
    public List<Integer> grades;

    public Student(String name, List<Integer> grades) {
        this.name = name;
        this.grades = grades;
    }

    public double average() {
        int sum = 0;
        for (int i : grades) {
            sum += i;
        }
        return (double) sum / grades.size();
    }

    public void PrintReport() {
        System.out.println("Имя студента" + name);
        System.out.println("Оценки" + grades);
        System.out.println("Средний балл" + average());
        double av = average();
        if (av >= 4.5) {
            System.out.println("Отличник");
        } else if (av >= 3.5) {
            System.out.println("Хорошист");
        } else if (av >= 2.5) {
            System.out.println("Троечник");
        } else {
            System.out.println("Двоечник");
        }
    }
}
