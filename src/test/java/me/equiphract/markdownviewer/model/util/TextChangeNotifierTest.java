package me.equiphract.markdownviewer.model.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class TextChangeNotifierTest {

  TextChangeNotifier changeNotifier = new TextChangeNotifier();

  @Test
  @SuppressWarnings("unchecked")
  void subscribe_givenSubscriber_notifiesSubscriber() {
    Object subscriber = new Object();
    Consumer<String> consumer = mock(Consumer.class);

    changeNotifier.subscribe(subscriber, consumer);
    changeNotifier.publish("");

    verify(consumer, only()).accept("");
  }

  @Test
  @SuppressWarnings("unchecked")
  void subscribe_givenNullSubscriber_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
                 () -> changeNotifier.subscribe(null, mock(Consumer.class)));
  }

  @Test
  void subscribe_givenNullCallback_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
                 () -> changeNotifier.subscribe(new Object(), null));
  }

  @Test
  @SuppressWarnings("unchecked")
  void unsubscribe_givenSubscriber_subscriberDoesntGetNotifiedAnymore() {
    Object subscriber = new Object();
    Consumer<String> consumer = mock(Consumer.class);

    changeNotifier.subscribe(subscriber, consumer);
    changeNotifier.publish("1");

    verify(consumer, only()).accept("1");

    changeNotifier.unsubscribe(subscriber);
    changeNotifier.publish("2");

    verify(consumer, never()).accept("2");
  }

}

