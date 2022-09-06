package me.equiphract.markdownviewer.model.util;

import java.util.function.Consumer;

public interface ChangeNotifier<T> {
  void subscribe(Object subscriber, Consumer<T> callback);
  void unsubscribe(Object subscriber);
  void publish(T update);
}

