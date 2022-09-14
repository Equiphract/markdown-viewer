package me.equiphract.markdownviewer.model.io;

import java.nio.file.Path;

public interface FileContentReader {

  String read(Path path);

}

