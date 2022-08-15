package me.equiphract.markdownviewer;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MarkdownViewerApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    final var scene = new Scene(createContent(), 300, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private Parent createContent() {
    return new StackPane(new Text("Markdown-Viewer"));
  }

}

