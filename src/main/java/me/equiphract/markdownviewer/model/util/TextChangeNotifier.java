package me.equiphract.markdownviewer.model.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class TextChangeNotifier implements ChangeNotifier<String> {

  private Map<Object, Consumer<String>> subscribers = new HashMap<>();

  @Override
  public void subscribe(Object subscriber, Consumer<String> callback) {
    if (subscriber == null || callback == null) {
      throw new IllegalArgumentException("Null values are not permitted.");
    }

    subscribers.put(subscriber, callback);
  }

  @Override
  public void unsubscribe(Object subscriber) {
    subscribers.remove(subscriber);
  }

  @Override
  public void publish(String changedFileContent) {
    subscribers.values().forEach(s -> s.accept(changedFileContent));
  }

}

