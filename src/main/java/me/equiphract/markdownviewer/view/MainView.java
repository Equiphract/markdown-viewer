package me.equiphract.markdownviewer.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import me.equiphract.markdownviewer.viewmodel.MainViewModel;

public final class MainView {

  private MainViewModel mainViewModel;
  private FileChooser fileChooser;
  private int horizontalScrollPosition;
  private int verticalScrollPosition;

  @FXML
  private WebView webView;
  private WebEngine webEngine;

  public MainView() throws IOException, InterruptedException {
    mainViewModel = new MainViewModel();
  }

  @FXML
  protected void initialize() {
    webEngine = webView.getEngine();

    addHtmlPropertyListener();
    addPageLoadingStatePropertyListener();
  }

  private void addHtmlPropertyListener() {
    mainViewModel.addAsyncHtmlPropertyListener(this::reloadHtmlOnChange);
  }

  private void reloadHtmlOnChange(
      ObservableValue<? extends String> htmlProperty,
      String oldValue,
      String newValue) {

    webEngine.loadContent(newValue);
  }

  private void addPageLoadingStatePropertyListener() {
    ReadOnlyProperty<State> pageLoadingStateProperty =
      webEngine.getLoadWorker().stateProperty();

    pageLoadingStateProperty
      .addListener(this::scrollToPreviousPositionIfPageLoaded);
  }

  private void scrollToPreviousPositionIfPageLoaded(
      ObservableValue<? extends State> stateProperty,
      State oldValue,
      State newValue) {

    if (newValue == State.SUCCEEDED) {
      scrollToLastKnownPosition();
    }
  }

  private void scrollToLastKnownPosition() {
    String jsScrollTo = "window.scrollTo(%s, %s)"
      .formatted(verticalScrollPosition, horizontalScrollPosition);

    webEngine.executeScript(jsScrollTo);
  }

  @FXML
  private void openFile(ActionEvent event)
      throws FileNotFoundException, IOException, InterruptedException {

    if (fileChooser == null) {
      createFileChooser();
    }

    Window mainWindow = webView.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(mainWindow);

    resetScrollPosition();
    mainViewModel.loadFile(selectedFile);
  }

  private void createFileChooser() {
    fileChooser = new FileChooser();
    fileChooser.setTitle("Open Markdown File");
  }

  private void resetScrollPosition() {
    horizontalScrollPosition = 0;
    verticalScrollPosition = 0;
  }

  @FXML
  private void rememberScrollPosition() {
    horizontalScrollPosition =
      (Integer) webEngine.executeScript("document.body.scrollTop");
    verticalScrollPosition =
      (Integer) webEngine.executeScript("document.body.scrollLeft");
  }

}

