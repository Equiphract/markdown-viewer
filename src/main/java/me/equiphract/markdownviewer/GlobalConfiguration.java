package me.equiphract.markdownviewer;

import java.nio.file.Path;

/**
 * This class is a temporary solution for passing configuration throughout the
 * application.
 */
public class GlobalConfiguration {
  private static final String USER_HOME_DIRECTORY =
      System.getProperty("user.home");
  private static final String APPLICATION_DIRECTORY = ".markdown-viewer";
  public static final Path STYLES_DIRECTORY =
      Path.of(USER_HOME_DIRECTORY, APPLICATION_DIRECTORY, "styles");
}
