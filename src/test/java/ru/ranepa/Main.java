package ru.ranepa;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите имя ученика");
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();
        int grade = -1;
        List<Integer> grades = new ArrayList<>();
        while (grade != 0) {
            System.out.println("Введите оценку ученика , 0 для выхода");
            grade = input.nextInt();
            if (grade != 0) {
                grades.add(grade);
            }
        }
        Student student = new Student(name, grades);
        student.PrintReport();
    }
}
