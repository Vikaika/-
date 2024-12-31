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
        inputArea.setEditable(false); // текстовая область как не редактируемая со стороны пользователя

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

        //создание радиокнопок для выбора формата записи
        JRadioButton txtWrite = new JRadioButton(".txt");
        add(txtWrite);
        txtWrite.setSelected(true);
        JRadioButton xmlWrite = new JRadioButton(".xml");
        add(xmlWrite);
        //группа гарантирует, что только одна из радиокнопок может быть выбрана в данный момент
        ButtonGroup writeGroup = new ButtonGroup();
        writeGroup.add(txtWrite);
        writeGroup.add(xmlWrite);

        outputArea = new JTextArea(); //объект JTextArea позволяет отображать и вводить многострочный текст
        outputArea.setBackground(new Color(255, 182, 193));
        outputArea.setFont(new Font("Calibri", Font.PLAIN, 14));
        add(outputArea);
        outputArea.setEditable(false);

        //обработка событий к кнопке read
        read.addActionListener(e -> {
            //выбираем файлы с диска
            JFileChooser fileChooser = new JFileChooser("D:\\ПП (Java)\\IProject\\IProject\\com\\example\\proj\\inputs");
            try {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    //Устанавливаем стратегию чтения/записи файлов в зависимости от формата
                    if (selectedFile.getName().endsWith("txt")) {
                        setStrategy(new TextFilesReadWrite());
                    }
                    else if (selectedFile.getName().endsWith("xml")) {
                        setStrategy(new XMLFilesReadWrite());
                    }
                    else {
                        throw new IllegalArgumentException("Unsupported file format.");
                    }
                    strings = method.getStringArray(selectedFile); //читаем данные из выбранного файла и возвращаем массив строк
                }
            }
            //неподдержанный формат файла
            catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(null, "Choose a correct file.", "Illegal file format!", JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException | ParserConfigurationException | SAXException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }
            showInput(); //отображение данных из файла
            strings = solveProblems(strings);
            showOutput(); //отображение результатов вычислений
        });

        //обработка события к кнопке write
        write.addActionListener(e -> {
            try {
                String dir = "D:\\ПП (Java)\\IProject\\IProject\\com\\example\\proj\\outputs";
                String fileName = JOptionPane.showInputDialog("Enter file name you want to save result to ");
                //предотвращаем попытку сохранить файл без имени
                if (fileName == null || fileName.isEmpty()) throw new IllegalArgumentException("File name is empty.");
                //выбор в каком формате сохраняем данные в переменной strings
                if (txtWrite.isSelected()) {
                    writeToTXT(dir, fileName + ".txt", strings);
                } else if (xmlWrite.isSelected()) {
                    writeToXML(dir, fileName + ".xml", strings);
                }
                JOptionPane.showMessageDialog(null, "File was written successfully!");
            } catch (IOException | ParserConfigurationException | TransformerException | IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }
        });        
    }

    //запись списка строк в текстовый файл
    public static void writeToTXT(String dir, String Fname, ArrayList<String> str) throws IOException {
        try (FileWriter fileWriter = new FileWriter(dir + Fname)) {
            for (String string : str) {
                fileWriter.write(string);
                fileWriter.write("\n");
            }
        }
    }

    //метод - запись списка строк в XML файл
    public static void writeToXML(String dir, String fileName, ArrayList<String> str) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("text");
        doc.appendChild(root);
        for (String string : str) {
            Element element = doc.createElement("string");
            element.setTextContent(string);
            root.appendChild(element);
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(new File(dir + fileName)));
    }


    //метод - вычисление матем выражений
    public ArrayList<String> solveProblems(ArrayList<String> str) {
        //ищем матем выражения
        Pattern pat = Pattern.compile("(\\s*-?\\s*(\\(*\\s*(-?\\s*\\d+(\\.\\d+)?)\\s*\\)*\\s*)*[-+*/^](\\s*\\(*\\s*(-?\\s*\\d+(\\.\\d+)?)\\s*\\)*\\s*)+)+");
        String found_str; //найденная подстрока, соответствующая регулярному выражению
        String new_str; //результат вычисления
        for (int i = 0; i < str.size(); i++) {
            //для поиска соответствий в текущей строке
            Matcher matcher = pat.matcher(str.get(i));
            while (matcher.find()) {
                found_str = str.get(i).substring(matcher.start(), matcher.end());
                new_str = calculator.Calculate(found_str); // Вызов метода Calculate на экземпляре калькулятора
                str.set(i, str.get(i).replace(found_str, new_str));
            }
        }
        return str;
    }

   private void Strategy(FileProcessing fileProcessing) {
        method = fileProcessing;
    }

    //метод - отображение входных данных в текстовой области inputArea
    private void showInput() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strings) {
            stringBuilder.append(str).append("\n");
        }
        inputArea.setText(stringBuilder.toString());
    }

    //метод - отображение выходных данных в текстовой области outputArea
    private void showOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append("\n");
        }
        outputArea.setText(stringBuilder.toString());
    }
    
    //запуск приложения Swing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window("Pink GUI");
            window.setSize(400, 300);
            window.setVisible(true);
        });
    }
}
