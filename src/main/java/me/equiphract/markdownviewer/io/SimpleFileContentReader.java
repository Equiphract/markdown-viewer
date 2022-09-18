package me.equiphract.markdownviewer.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import me.equiphract.markdownviewer.model.io.FileContentReader;

public class SimpleFileContentReader implements FileContentReader {

  @Override
  public String read(String filePath)
      throws FileNotFoundException, IOException {

    return Files.readString(Path.of(filePath));
  }

}
