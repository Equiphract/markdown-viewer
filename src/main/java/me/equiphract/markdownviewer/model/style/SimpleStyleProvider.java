package me.equiphract.markdownviewer.model.style;

import java.nio.file.Path;

import me.equiphract.markdownviewer.model.io.FileContentReader;

public final class SimpleStyleProvider implements StyleProvider {

  private String stylesDirectory;
  private FileContentReader fileContentReader;

  public SimpleStyleProvider(
      String stylesDirectory, FileContentReader fileContentReader) {

    if (stylesDirectory == null || fileContentReader == null) {
      throw new IllegalArgumentException("Null arguments are not allowed.");
    }

    this.stylesDirectory = stylesDirectory;
    this.fileContentReader = fileContentReader;
  }

  @Override
  public String getStyle(String styleName) {
    if (styleName == null) {
      throw new IllegalArgumentException("The style name must not be null.");
    }

    Path pathToFile = Path.of(stylesDirectory, styleName);
    return fileContentReader.read(pathToFile);
  }

}

