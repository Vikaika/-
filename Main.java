package com.example.proj;
public class Main {
    public static void main(String[] args) {
        Window chooser = new Window("Semester project: calculations");
        chooser.pack();
        chooser.setSize(300, 400); //размер окна для калькулятора
        chooser.setLocation(20, screenHeight - 400); //размещение окна
        chooser.setVisible(true);
    }
}