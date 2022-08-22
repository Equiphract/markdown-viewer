package me.equiphract.markdownviewer.viewmodel;

import java.io.IOException;
import java.nio.file.Path;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import me.equiphract.markdownviewer.model.io.FileObserver;
import me.equiphract.markdownviewer.model.markdown.MarkdownConverter;
import me.equiphract.markdownviewer.model.markdown.MarkdownToHtmlConverter;

public final class MainViewModel {

  private StringProperty pageHtml;
  private FileObserver fileObserver;
  private MarkdownConverter converter;

  public MainViewModel() throws IOException, InterruptedException {
    pageHtml = new SimpleStringProperty("");
    fileObserver = new FileObserver();
    converter = new MarkdownToHtmlConverter();

    fileObserver.subscribeToChangeNotifier(this, this::updatePageHtmlProperty);
    Path file = Path.of("/tmp/markdown.md");
    fileObserver.observe(file);
  }

  private void updatePageHtmlProperty(String modifiedFileContent) {
    String convertedFileContent = converter.convert(modifiedFileContent);
    pageHtml.set(convertedFileContent);
  }

  public void addAsyncPageHtmlPropertyListener(
      ChangeListener<? super String> listener) {

    wrapListenerInRunLater(listener);
    pageHtml.addListener(wrapListenerInRunLater(listener));
  }

  private ChangeListener<? super String> wrapListenerInRunLater(
      ChangeListener<? super String> listener) {

    return (observable, oldValue, newValue) -> {
      Platform.runLater(() -> listener.changed(observable, oldValue, newValue));
    };
  }

}

