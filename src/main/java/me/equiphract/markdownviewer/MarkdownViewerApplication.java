package me.equiphract.markdownviewer;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class MarkdownViewerApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    configurePrimaryStage(primaryStage);
    primaryStage.show();
  }

  private void configurePrimaryStage(Stage primaryStage) throws IOException {
    primaryStage.setScene(buildMainScene());
    primaryStage.setTitle("Markdown-Viewer");
  }

  private Scene buildMainScene() throws IOException {
    var fxmlLoader = new FXMLLoader(getMainFxmlLocationUrl());
    var sceneRootObject = (Parent) fxmlLoader.load();
    return new Scene(sceneRootObject, 300, 600);
  }

  private URL getMainFxmlLocationUrl() {
    String mainFxmlLocation = "view/MainView.fxml";
    return getClass().getResource(mainFxmlLocation);
  }

}

