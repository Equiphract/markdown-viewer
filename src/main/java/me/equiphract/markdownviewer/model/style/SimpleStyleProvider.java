package me.equiphract.markdownviewer.model.style;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import me.equiphract.markdownviewer.model.io.FileContentReader;

public final class SimpleStyleProvider implements StyleProvider {

  private Path stylesDirectoryPath;
  private FileContentReader fileContentReader;

  public SimpleStyleProvider(
      String stylesDirectory, FileContentReader fileContentReader) {

    if (stylesDirectory == null || fileContentReader == null) {
      throw new IllegalArgumentException("Null arguments are not allowed.");
    }

    this.stylesDirectoryPath = Path.of(stylesDirectory);
    this.fileContentReader = fileContentReader;
  }

  @Override
  public Optional<String> getStyle(String filename) {
    validateFilename(filename);

    var pathToFile = stylesDirectoryPath.resolve(filename);
    var pathAsString = pathToFile.toString();

    return tryToRead(pathAsString);
  }

  private void validateFilename(String styleName) {
    if (styleName == null) {
      throw new IllegalArgumentException("The filename must not be null.");
    }
  }

  private Optional<String> tryToRead(String filePath) {
    Optional<String> style = Optional.empty();

    try {
      String styleString = fileContentReader.read(filePath);
      style = Optional.of(styleString);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(filePath + " could not be read from.");
    }

    return style;
  }

}

