//чтение содержимого XML-файлов
package com.example.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; // Добавлено
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Чтение строк из XML-файла и возврат их в виде списка
public class XMLFilesReadWrite implements FileProcessing {
    @Override
    public ArrayList<String> getStringArray(File file) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> text = new ArrayList<>();
        // Создание нового объекта DocumentBuilderFactory, который может создавать парсеры
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        // parse() читает XML-документ и создаёт объект document
        Document document = builderFactory.newDocumentBuilder().parse(file);
        // Извлечение всех элементов с тегом <string>
        NodeList stringNodeList = document.getElementsByTagName("string");
        // Перебор всех узлов с тегом <string> и извлечение текстового содержимого в список text
        for (int i = 0; i < stringNodeList.getLength(); i++) {
            Node stringNode = stringNodeList.item(i);
            text.add(stringNode.getTextContent());
        }
        return text;
    }
}
