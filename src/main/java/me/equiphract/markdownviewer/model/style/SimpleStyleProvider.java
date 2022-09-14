package me.equiphract.markdownviewer.model.style;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class SimpleStyleProvider implements StyleProvider {

  public String getStyle(BufferedReader reader)
      throws FileNotFoundException, IOException {

    return readFile(reader);
  }

  private String readFile(BufferedReader reader) throws IOException {
    if (reader == null) {
      throw new IllegalArgumentException("A null argument is not allowed.");
    }

    var stringBuilder = new StringBuilder(1000);

    String line;
    while ((line = reader.readLine()) != null) {
      stringBuilder.append(line + "\n");
    }

    return stringBuilder.toString();
  }

}

