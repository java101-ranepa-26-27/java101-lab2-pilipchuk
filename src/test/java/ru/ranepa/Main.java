package ru.ranepa;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter student name");
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();
        int grade = -1;
        List<Integer> grades = new ArrayList<>();
        while (grade != 0){
            System.out.println("Enter student grade or zero");
            grade= input.nextInt();
            grades.add(grade);
        }

    }
}
