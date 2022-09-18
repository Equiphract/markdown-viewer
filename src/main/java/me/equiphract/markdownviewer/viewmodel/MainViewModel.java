package me.equiphract.markdownviewer.viewmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import me.equiphract.markdownviewer.io.SimpleFileContentReader;
import me.equiphract.markdownviewer.io.SingleFileObserver;
import me.equiphract.markdownviewer.model.htmlbuilder.HtmlBuilder;
import me.equiphract.markdownviewer.model.htmlbuilder.SimpleHtmlBuilder;
import me.equiphract.markdownviewer.model.io.FileObserver;
import me.equiphract.markdownviewer.model.markdown.MarkdownConverter;
import me.equiphract.markdownviewer.model.markdown.MarkdownToHtmlConverter;
import me.equiphract.markdownviewer.model.style.SimpleStyleProvider;
import me.equiphract.markdownviewer.model.style.StyleProvider;


public final class MainViewModel {

  private StringProperty html;
  private StringProperty stylesDirectory;
  private StringProperty currentlyUsedStyleFilename;
  private ListProperty<String> styleFilenames;
  private FileObserver fileObserver;
  private Path currentlyObservedFilePath;
  private MarkdownConverter converter;
  private HtmlBuilder htmlBuilder;
  private StyleProvider styleProvider;
  private String convertedFileContent;

  public MainViewModel() throws IOException, InterruptedException {
    html = new SimpleStringProperty();
    stylesDirectory = new SimpleStringProperty();
    currentlyUsedStyleFilename = new SimpleStringProperty();
    styleFilenames = new SimpleListProperty<String>(
        FXCollections.observableList(new LinkedList<String>()));
    WatchService watchService = FileSystems.getDefault().newWatchService();
    fileObserver = new SingleFileObserver(watchService);
    converter = new MarkdownToHtmlConverter();
    htmlBuilder = new SimpleHtmlBuilder();

    addStylesDirectoryListeners();
    addCurrentlyUsedStyleFilenameListener();
    subscribeToFileObserver();
  }

  private void addStylesDirectoryListeners() {
    stylesDirectory.addListener(this::buildNewStyleProvider);
    stylesDirectory.addListener(this::updateStyleFilenames);
  }

  private void buildNewStyleProvider(
      ObservableValue<? extends String> stylesDirectoryProperty,
      String oldStylesDirectory,
      String newStylesDirectory) {

    styleProvider = new SimpleStyleProvider(
        newStylesDirectory, new SimpleFileContentReader());
  }

  private void updateStyleFilenames(
      ObservableValue<? extends String> stylesDirectoryProperty,
      String oldStylesDirectory,
      String newStylesDirectory) {

    List<String> filenames = getStyleFilenamesInDirectory(newStylesDirectory);
    styleFilenames.addAll(filenames);
  }

  private List<String> getStyleFilenamesInDirectory(String directoryPath) {
    var stylesDirectoryPath = Path.of(directoryPath);
    List<String> filenames = new LinkedList<>();

    try (var directoryStream = Files.newDirectoryStream(stylesDirectoryPath)) {

      directoryStream.forEach(entry -> {
        var filename = entry.getFileName().toString();
        if (filename.endsWith(".css")) {
          filenames.add(filename);
        }
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    return filenames;
  }

  private void addCurrentlyUsedStyleFilenameListener() {
    currentlyUsedStyleFilename.addListener(
        (observable, oldStyleFilename, newStyleFilename) -> renderHtml());
  }

  private void renderHtml() {
    String constructedHtml = constructHtml(convertedFileContent);
    html.set(constructedHtml);
  }

  private void subscribeToFileObserver() {
    fileObserver.subscribe(this, this::updateHtml);
  }

  private void updateHtml(String modifiedFileContent) {
    // TODO this callback gets executed multiple times for a single file edit by
    // the FileObserver for some reason...
    convertedFileContent = converter.convert(modifiedFileContent);
    renderHtml();
  }

  private String constructHtml(String convertedFileContent) {
    String parentPath = currentlyObservedFilePath.getParent().toString();

    var styleFilename = this.currentlyUsedStyleFilename.getValueSafe();
    var style = "";

    if (!styleFilename.isEmpty()) {
      style = styleProvider.getStyle(styleFilename).orElse("");
    }

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

  public void addStyleFilenamesPropertyListener(
      ListChangeListener<? super String> listener) {

    styleFilenames.addListener(listener);
  }

  public void bindBidirectionalToStylesDirectoryProperty(
      Property<String> other) {

    stylesDirectory.bindBidirectional(other);
  }

  public void bindUnidirectionalToCurrentlyUsedStyleFilenameProperty(
      StringProperty other) {

    this.currentlyUsedStyleFilename.bind(other);
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

