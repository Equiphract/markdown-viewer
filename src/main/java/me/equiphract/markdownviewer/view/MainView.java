package me.equiphract.markdownviewer.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import me.equiphract.markdownviewer.viewmodel.MainViewModel;

public final class MainView {

  private MainViewModel mainViewModel;
  private FileChooser fileChooser;

  @FXML
  private WebView webView;

  public MainView() throws IOException, InterruptedException {
    mainViewModel = new MainViewModel();
  }

  @FXML
  protected void initialize() {
    addHtmlPropertyListener();
  }

  private void addHtmlPropertyListener() {
    mainViewModel.addAsyncHtmlPropertyListener(this::reloadHtmlOnChange);
  }

  private void reloadHtmlOnChange(
      ObservableValue<? extends String> htmlProperty,
      String oldValue,
      String newValue) {

    webView.getEngine().loadContent(newValue);
  }

  @FXML
  private void openFile(ActionEvent event)
      throws FileNotFoundException, IOException, InterruptedException {

    if (fileChooser == null) {
      createFileChooser();
    }

    Window mainWindow = webView.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(mainWindow);

    mainViewModel.loadFile(selectedFile);
  }

  private void createFileChooser() {
    fileChooser = new FileChooser();
    fileChooser.setTitle("Open Markdown File");
  }

}

