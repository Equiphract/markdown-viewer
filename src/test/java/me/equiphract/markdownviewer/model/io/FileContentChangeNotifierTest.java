package me.equiphract.markdownviewer.model.io;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class FileContentChangeNotifierTest {

  FileContentChangeNotifier listener = new FileContentChangeNotifier();

  @Test
  @SuppressWarnings("unchecked")
  void subscribe_givenSubscriber_notifiesSubscriber() {
    Object subscriber = new Object();
    Consumer<String> consumer = mock(Consumer.class);

    listener.subscribe(subscriber, consumer);
    listener.publish("");

    verify(consumer, only()).accept("");
  }

  @Test
  @SuppressWarnings("unchecked")
  void subscribe_givenNullSubscriber_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
                 () -> listener.subscribe(null, mock(Consumer.class)));
  }

  @Test
  void subscribe_givenNullCallback_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
                 () -> listener.subscribe(new Object(), null));
  }

  @Test
  @SuppressWarnings("unchecked")
  void unsubscribe_givenSubscriber_subscriberDoesntGetNotifiedAnymore() {
    Object subscriber = new Object();
    Consumer<String> consumer = mock(Consumer.class);

    listener.subscribe(subscriber, consumer);
    listener.publish("1");

    verify(consumer, only()).accept("1");

    listener.unsubscribe(subscriber);
    listener.publish("2");

    verify(consumer, never()).accept("2");
  }

}

