package me.equiphract.markdownviewer.view;

import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import me.equiphract.markdownviewer.viewmodel.MainViewModel;

public final class MainView {

  private MainViewModel mainViewModel;

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
    mainViewModel.addAsyncPageHtmlPropertyListener(this::reloadHtmlOnChange);
  }

  private void reloadHtmlOnChange(
      ObservableValue<? extends String> htmlProperty,
      String oldValue,
      String newValue) {
    webView.getEngine().loadContent(newValue);
  }

}

