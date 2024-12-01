//чтение и запись файлов (для разных форматов)
package com.example.proj;

import java.io.File;
import java.io.IOException;

public interface ReadWriteFiles {
    String read(File file) throws IOException;
    void write(File file, String str) throws IOException;
}
