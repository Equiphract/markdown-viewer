package me.equiphract.markdownviewer.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import me.equiphract.markdownviewer.viewmodel.MainViewModel;

public final class MainView {

  private MainViewModel mainViewModel;
  private FileChooser fileChooser;
  private StringProperty stylesDirectory;
  private StringProperty styleFilename;
  private int horizontalScrollPosition;
  private int verticalScrollPosition;

  @FXML
  private Menu stylesMenu;

  @FXML
  private WebView webView;
  private WebEngine webEngine;

  public MainView() throws IOException, InterruptedException {
    mainViewModel = new MainViewModel();
    stylesDirectory = new SimpleStringProperty();
    styleFilename = new SimpleStringProperty();
  }

  @FXML
  protected void initialize() {
    setWebEngine();
    addPropertyListeners();
    bindProperties();
  }


  private void setWebEngine() {
    webEngine = webView.getEngine();
  }

  private void addPropertyListeners() {
    addHtmlPropertyListener();
    addPageLoadingStatePropertyListener();
    addStyleFilenamesPropertyListener();
  }

  private void addHtmlPropertyListener() {
    mainViewModel.addAsyncHtmlPropertyListener(this::reloadHtmlOnChange);
  }

  private void reloadHtmlOnChange(
      ObservableValue<? extends String> htmlProperty,
      String oldHtml,
      String newHtml) {

    webEngine.loadContent(newHtml);
  }

  private void addPageLoadingStatePropertyListener() {
    ReadOnlyProperty<State> pageLoadingStateProperty =
      webEngine.getLoadWorker().stateProperty();

    pageLoadingStateProperty
      .addListener(this::scrollToPreviousPositionIfPageLoaded);
  }

  private void scrollToPreviousPositionIfPageLoaded(
      ObservableValue<? extends State> stateProperty,
      State oldState,
      State newState) {

    if (newState == State.SUCCEEDED) {
      scroll(verticalScrollPosition, horizontalScrollPosition);
    }
  }

  private void scroll(int vertical, int horizontal) {
    String jsScrollTo = "window.scrollTo(%s, %s)"
      .formatted(vertical, horizontal);

    webEngine.executeScript(jsScrollTo);
  }

  private void bindProperties() {
    bindAndSetStylesDirectoryProperty();
    bindStyleFilenameProperty();
  }

  private void bindAndSetStylesDirectoryProperty() {
    mainViewModel.bindBidirectionalToStylesDirectoryProperty(stylesDirectory);
    stylesDirectory.set("/tmp/styles/");
  }

  private void bindStyleFilenameProperty() {
    mainViewModel
      .bindUnidirectionalToCurrentlyUsedStyleFilenameProperty(styleFilename);
  }

  private void addStyleFilenamesPropertyListener() {
    mainViewModel
      .addStyleFilenamesPropertyListener(this::buildStylesDirectoryMenu);
  }

  private void buildStylesDirectoryMenu(Change<? extends String> change) {
    change.getList().forEach(this::createStyleMenuItem);
  }

  private void createStyleMenuItem(String filename) {
    var styleMenuItem = new MenuItem(filename);
    styleMenuItem.setOnAction(this::setStyleFilename);
    stylesMenu.getItems().add(styleMenuItem);
  }

  private void setStyleFilename(ActionEvent event) {
    MenuItem source = (MenuItem) event.getSource();
    styleFilename.set(source.getText());
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

