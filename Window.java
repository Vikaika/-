package com.example.proj;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Window extends JFrame {

    private ArrayList<String> strings;    //строки, считанные из файла
    private FileProcessing method;        // интерфейс для реализации чтения и записи файлов
    private final JTextArea inputArea;    //область для отображения входных данных
    private final JTextArea outputArea;   //область для отображения выходных данных
    private final Calculator calculator;  // Экземпляр калькулятора

    Window(String name) {
        super(name);
        strings = new ArrayList<>();
        calculator = new Calculator();
        setDefaultCloseOperation(EXIT_ON_CLOSE); //завершаем приложение, когда пользователь закрывает окно
        getContentPane().setBackground(new Color(255, 228, 225)); // молочный цвет
        setLayout(new FlowLayout());

        inputArea = new JTextArea();
        inputArea.setBackground(new Color(255, 228, 225)); // молочный цвет
        inputArea.setFont(new Font("Calibri", Font.PLAIN, 14));
        add(inputArea);
        inputArea.setEditable(false); // текстовуая область как не редактируемая со стороны пользователя

        //кнопка чтения
        JButton read = new JButton("Read");
        read.setBackground(new Color(221, 160, 221));
        read.setFont(new Font("Calibri", Font.BOLD, 14));
        add(read);

        //кнопка записи
        JButton write = new JButton("Write");
        write.setBackground(new Color(221, 160, 221));
        write.setFont(new Font("Calibri", Font.BOLD, 14));
        add(write);
    }
}
