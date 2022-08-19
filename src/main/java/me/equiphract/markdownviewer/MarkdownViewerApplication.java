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
    final var scene = new Scene(buildInitialSceneContent(), 300, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private Parent buildInitialSceneContent() throws IOException {
    var fxmlLoader = new FXMLLoader(getInitialFxmlLocationUrl());
    return (Parent) fxmlLoader.load();
  }

  private URL getInitialFxmlLocationUrl() {
    String initialFxmlLocation = "view/PageRenderView.fxml";
    return getClass().getResource(initialFxmlLocation);
  }

}

