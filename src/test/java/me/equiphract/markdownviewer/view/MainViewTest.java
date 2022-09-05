package me.equiphract.markdownviewer.view;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class MainViewTest {

  @Start
  private void start(Stage stage) throws IOException {
    var fxmlLoader = new FXMLLoader(getMainFxmlLocationUrl());
    var sceneRootObject = (Parent) fxmlLoader.load();
    var mainScene = new Scene(sceneRootObject, 300, 600);
    stage.setScene(mainScene);
    stage.show();
  }

  private URL getMainFxmlLocationUrl() {
    String mainFxmlLocation = "/me/equiphract/markdownviewer/view/MainView.fxml";
    return getClass().getResource(mainFxmlLocation);
  }

  @Test
  void webView_shouldExist() {
    verifyThat("#webView", isVisible());
  }

  @Test
  void menuBar_shouldExist() {
    verifyThat("#menuBar", isVisible());
  }

  // @Test
  // void open_shouldStartFileExplorer(FxRobot robot) {
  //   robot.clickOn("#openMenuItem");
  //   // TODO don't know how to test...
  // }

}

