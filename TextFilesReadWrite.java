//чтение содержимого текстовых файлов
package com.example.proj;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//чтение строк из файла и возвращение их в виде списка 
public class TextFilesReadWrite implements FileProcessing {
    @Override
    //чтение строк из файла и возвращение их в виде списка
    public ArrayList<String> getStringArray(File file) throws FileNotFoundException {
        ArrayList<String> text = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            text.add(scanner.nextLine());
        }

        return text;
    }
}
