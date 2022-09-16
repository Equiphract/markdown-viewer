package me.equiphract.markdownviewer.model.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface FileObserver {
  void observe(Path file)
      throws FileNotFoundException, IOException, InterruptedException;
  Path getObservedPath();
  void subscribe(Object subscriber, Consumer<String> callback);
  void unsubscribe(Object subscriber);
}

