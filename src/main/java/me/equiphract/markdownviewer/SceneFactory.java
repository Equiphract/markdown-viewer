package me.equiphract.markdownviewer;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

// TODO How to write meaningful unit tests for such a class?
public class SceneFactory {

  public Scene createMainScene() throws IOException {
    var fxmlLoader = new FXMLLoader(getMainFxmlLocationUrl());
    var sceneRootObject = (Parent) fxmlLoader.load();
    return new Scene(sceneRootObject, 300, 600);
  }

  private URL getMainFxmlLocationUrl() {
    String mainFxmlLocation = "view/MainView.fxml";
    return getClass().getResource(mainFxmlLocation);
  }

}

