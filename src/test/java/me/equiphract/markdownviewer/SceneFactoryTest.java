package me.equiphract.markdownviewer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class SceneFactoryTest {

  private Scene mainScene;

  @Start
  private void start(Stage stage) throws IOException {
    // I was not able to figure out how to run the factory's create methods in
    // their own respective unit tests, so I have to do it here... The problem
    // with trying to run them inside the test methods, which are executed on
    // some JUnit threads, is that they need to be run on the JavaFX application
    // thread or else and exception gets thrown.
    SceneFactory sceneFactory = new SceneFactory();
    mainScene = sceneFactory.createMainScene();
  }

  @Test
  void factoryCreateMethods_shouldReturnScene() {
    assertNotNull(mainScene);
  }

}

