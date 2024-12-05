package com.example.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLFilesReadWrite implements FileProcessing {
        ArrayList<String> text = new ArrayList<>();
        /* создание нового объекта DocumentBuilderFactory,
         который может создавать парсеры */
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        /* parse() читает XML-документ и создаёт объект document,
        который содержит дерево элементов XML и содержит данные, полученные в результате парсинга */
        Document document = builderFactory.newDocumentBuilder().parse(file);

}
