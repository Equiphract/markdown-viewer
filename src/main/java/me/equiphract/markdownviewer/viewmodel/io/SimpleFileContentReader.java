package me.equiphract.markdownviewer.viewmodel.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import me.equiphract.markdownviewer.model.io.FileContentReader;

public class SimpleFileContentReader implements FileContentReader {

  @Override
  public String read(String filePath)
      throws FileNotFoundException, IOException {

    var stringBuilder = new StringBuilder(1000);

    try (var bufferedReader = new BufferedReader(new FileReader(filePath))) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }
    }

    return stringBuilder.toString();
  }

}
