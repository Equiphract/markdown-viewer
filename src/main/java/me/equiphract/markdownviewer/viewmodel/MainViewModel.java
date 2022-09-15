package me.equiphract.markdownviewer.viewmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import me.equiphract.markdownviewer.model.htmlbuilder.HtmlBuilder;
import me.equiphract.markdownviewer.model.htmlbuilder.SimpleHtmlBuilder;
import me.equiphract.markdownviewer.model.io.FileObserver;
import me.equiphract.markdownviewer.model.io.SingleFileObserver;
import me.equiphract.markdownviewer.model.markdown.MarkdownConverter;
import me.equiphract.markdownviewer.model.markdown.MarkdownToHtmlConverter;
import me.equiphract.markdownviewer.model.style.SimpleStyleProvider;
import me.equiphract.markdownviewer.model.style.StyleProvider;
import me.equiphract.markdownviewer.viewmodel.io.SimpleFileContentReader;


public final class MainViewModel {

  private static final String STYLES_DIRECTORY = "/tmp/styles/";
  private static final String DEFAULT_STYLE_FILENAME = "vanilla.css";

  private StringProperty html;
  private FileObserver fileObserver;
  private Path currentlyObservedFilePath;
  private MarkdownConverter converter;
  private HtmlBuilder htmlBuilder;
  private StyleProvider styleProvider;

  public MainViewModel() throws IOException, InterruptedException {
    html = new SimpleStringProperty("");
    WatchService watchService = FileSystems.getDefault().newWatchService();
    fileObserver = new SingleFileObserver(watchService);
    converter = new MarkdownToHtmlConverter();
    htmlBuilder = new SimpleHtmlBuilder();
    var fileContentReader = new SimpleFileContentReader();
    styleProvider =
      new SimpleStyleProvider(STYLES_DIRECTORY, fileContentReader);

    fileObserver.subscribe(this, this::updateHtml);
  }

  private void updateHtml(String modifiedFileContent) {
    // TODO this callback gets executed multiple times for a single file edit by
    // the FileObserver for some reason...
    String convertedFileContent = converter.convert(modifiedFileContent);
    String constructedHtml = constructHtml(convertedFileContent);
    html.set(constructedHtml);
  }

  private String constructHtml(String convertedFileContent) {
    String parentPath = currentlyObservedFilePath.getParent().toString();
    String style = styleProvider.getStyle(DEFAULT_STYLE_FILENAME).orElse("");
    return htmlBuilder.build(parentPath, style, convertedFileContent);
  }

  public void addAsyncHtmlPropertyListener(
      ChangeListener<? super String> listener) {

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
      currentlyObservedFilePath = filePath;
      updateHtml(Files.readString(filePath));
    }
  }

}

