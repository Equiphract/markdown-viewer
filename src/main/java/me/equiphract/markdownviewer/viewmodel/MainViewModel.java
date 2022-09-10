package me.equiphract.markdownviewer.viewmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.WatchService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import me.equiphract.markdownviewer.model.io.FileObserver;
import me.equiphract.markdownviewer.model.io.SingleFileObserver;
import me.equiphract.markdownviewer.model.markdown.MarkdownConverter;
import me.equiphract.markdownviewer.model.markdown.MarkdownToHtmlConverter;

public final class MainViewModel {

  private StringProperty html;
  private FileObserver fileObserver;
  private MarkdownConverter converter;

  public MainViewModel() throws IOException, InterruptedException {
    html = new SimpleStringProperty("");
    WatchService watchService = FileSystems.getDefault().newWatchService();
    fileObserver = new SingleFileObserver(watchService);
    converter = new MarkdownToHtmlConverter();

    fileObserver.subscribe(this, this::updateHtml);
  }

  private void updateHtml(String modifiedFileContent) {
    String convertedFileContent = converter.convert(modifiedFileContent);
    html.set(convertedFileContent);
  }

  public void addAsyncHtmlPropertyListener(
      ChangeListener<? super String> listener) {

    wrapListenerInRunLater(listener);
    html.addListener(wrapListenerInRunLater(listener));
  }

  private ChangeListener<? super String> wrapListenerInRunLater(
      ChangeListener<? super String> listener) {

    return (observable, oldValue, newValue) -> {
      Platform.runLater(() -> listener.changed(observable, oldValue, newValue));
    };
  }

  public void loadFile(File file)
      throws FileNotFoundException, IOException, InterruptedException {

    if (file != null) {
      var filePath = file.toPath();
      fileObserver.observe(filePath);
      updateHtml(Files.readString(filePath));
    }
  }

}

