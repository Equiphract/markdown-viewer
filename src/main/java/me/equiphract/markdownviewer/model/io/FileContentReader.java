package me.equiphract.markdownviewer.model.io;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileContentReader {

  String read(String filePath) throws FileNotFoundException, IOException;

}

