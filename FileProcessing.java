package com.example.proj;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface FileProcessing {
    ArrayList<String> getStringArray(File file) throws IOException, ParserConfigurationException, SAXException;
}
