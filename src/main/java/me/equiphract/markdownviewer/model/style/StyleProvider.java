package me.equiphract.markdownviewer.model.style;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface StyleProvider {

  String getStyle(BufferedReader reader)
      throws FileNotFoundException, IOException;

}

