package me.equiphract.markdownviewer.model.style;

import java.util.Optional;

public interface StyleProvider {

  Optional<String> getStyle(String filename);

}

