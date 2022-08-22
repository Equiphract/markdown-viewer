package me.equiphract.markdownviewer.model.io;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class FileContentChangeNotifier {

  private Map<Object, Consumer<String>> subscribers = new HashMap<>();

  public void subscribe(Object subscriber, Consumer<String> callback) {
    if (subscriber == null || callback == null) {
      throw new IllegalArgumentException("Null values are not permitted.");
    }

    subscribers.put(subscriber, callback);
  }

  public void publish(String changedFileContent) {
    subscribers.values().forEach(s -> s.accept(changedFileContent));
  }

  public void unsubscribe(Object subscriber) {
    subscribers.remove(subscriber);
  }

}

