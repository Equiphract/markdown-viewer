package me.equiphract.markdownviewer.io;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import me.equiphract.markdownviewer.model.io.FileObserver;
import me.equiphract.markdownviewer.model.util.ChangeNotifier;
import me.equiphract.markdownviewer.model.util.TextChangeNotifier;

/**
 * FileObserver instances observe a single file in the filesystem and notify via
 * a {@link ChangeNotifier} if any changes to the file's content occur or if the
 * file gets deleted.
 */
public final class SingleFileObserver implements FileObserver {

  private ChangeNotifier<String> changeNotifier;
  private WatchService watchService;
  private WatchKey currentlyRegisteredWatchKey;
  private ExecutorService singleThreadPool;
  private Path file;
  private boolean isThreadPoolActive;

  public SingleFileObserver(WatchService watchService)
      throws IOException {

    if (watchService == null) {
      throw new IllegalArgumentException("WatchService must not be null.");
    }

    changeNotifier = new TextChangeNotifier();
    this.watchService = watchService;
    singleThreadPool = Executors.newSingleThreadExecutor(this::useDaemonThread);
  }

  private Thread useDaemonThread(Runnable runnable) {
    Thread thread = Executors.defaultThreadFactory().newThread(runnable);
    thread.setDaemon(true);
    return thread;
  }

  @Override
  public void observe(Path file)
      throws FileNotFoundException, IOException, InterruptedException {

    if (file == null) {
      throw new IllegalArgumentException("File path must not be null.");
    }

    this.file = file;
    verifyThatFileExists();
    cancelPreviousWatchServiceRegistrationIfPresent();
    registerWatchService();
    startNewWatchServiceThreadIfNotAlreadyRunning();
  }

  private void verifyThatFileExists() throws FileNotFoundException {
    if (Files.notExists(file)) {
      throw new FileNotFoundException(file.toString());
    }
  }

  private void cancelPreviousWatchServiceRegistrationIfPresent() {
    if (currentlyRegisteredWatchKey != null) {
      currentlyRegisteredWatchKey.cancel();
    }
  }

  private void registerWatchService() throws IOException {
    var fileDirectory = file.getParent();
    currentlyRegisteredWatchKey =
      fileDirectory.register(watchService, ENTRY_MODIFY, ENTRY_DELETE);
  }

  private void startNewWatchServiceThreadIfNotAlreadyRunning() {
    if (!isThreadPoolActive) {
      singleThreadPool.execute(() -> tryRunWatchService());
      isThreadPoolActive = true;
    }
  }

  private void tryRunWatchService() {
    try {
      runWatchService();
    } catch (IOException e) {
      changeNotifier.publish(
          "There was an error while reading '%s'".formatted(file));
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void runWatchService() throws InterruptedException, IOException {
    WatchKey watchKey;
    while(true) {
      watchKey = watchService.take();
      pollForEvents(watchKey);
      watchKey.reset();
    }
  }

  private void pollForEvents(WatchKey watchKey) throws IOException {
    for (WatchEvent<?> event : watchKey.pollEvents()) {
      handleWatchEvent(event);
    }
  }

  private void handleWatchEvent(WatchEvent<?> event) throws IOException {
    if (isFileDeletedEvent(event)) {
      notifyOfFileDeletion();
    } else if (isFileModifiedEvent(event)) {
      notifyOfFileModification();
    }
  }

  private boolean isFileDeletedEvent(WatchEvent<?> event) {
    return event.kind().equals(ENTRY_DELETE)
      && event.context().equals(file.getFileName());
  }

  private void notifyOfFileDeletion() throws IOException {
    changeNotifier.publish(""); // TODO somehow relay file deletion event
  }

  private boolean isFileModifiedEvent(WatchEvent<?> event) {
    return event.kind().equals(ENTRY_MODIFY)
      && event.context().equals(file.getFileName());
  }

  private void notifyOfFileModification() throws IOException {
    changeNotifier.publish(Files.readString(file));
  }

  @Override
  public Path getObservedPath() {
    return file;
  }

  @Override
  public void subscribe(Object subscriber, Consumer<String> callback) {
    changeNotifier.subscribe(subscriber, callback);
  }

  @Override
  public void unsubscribe(Object subscriber) {
    changeNotifier.unsubscribe(subscriber);
  }

}

