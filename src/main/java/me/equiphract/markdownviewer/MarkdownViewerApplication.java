package me.equiphract.markdownviewer;

import java.io.IOException;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.stage.Stage;

public final class MarkdownViewerApplication extends Application {

  private SceneFactory sceneFactory = new SceneFactory();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    createApplicationStyleDirectoryIfNecessary();
    configurePrimaryStage(primaryStage);
    primaryStage.show();
  }

  private void createApplicationStyleDirectoryIfNecessary() throws IOException {
    Files.createDirectories(GlobalConfiguration.STYLES_DIRECTORY);
  }

  private void configurePrimaryStage(Stage primaryStage) throws IOException {
    primaryStage.setScene(sceneFactory.createMainScene());
    primaryStage.setTitle("Markdown-Viewer");
  }



}

