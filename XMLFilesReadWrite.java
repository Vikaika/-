//чтение содержимого XML-файлов
package com.example.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//чтение строк из XML-файла и возврат их в виде списка
public class XMLFilesReadWrite implements FileProcessing {
    @Override
    public ArrayList<String> getStringArray(File file) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> text = new ArrayList<>();
        /* создание нового объекта DocumentBuilderFactory,
         который может создавать парсеры */
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        /* parse() читает XML-документ и создаёт объект document,
        который содержит дерево элементов XML и содержит данные, полученные в результате парсинга */
        Document document = builderFactory.newDocumentBuilder().parse(file);
        /* извлекаются все элементы с тегом <string>
        и помещаются в объект NodeList */
        NodeList stringNodeList = document.getElementsByTagName("string");
        /* перебор всех узлов с тегом <string> извлечение текстового содержимого
        в список text */
        for (int i = 0; i < stringNodeList.getLength(); i++) {
            Node stringNode = stringNodeList.item(i);
            text.add(stringNode.getTextContent());
        }
        return text;
    }
}
