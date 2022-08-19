package me.equiphract.markdownviewer.model.filereader;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * FileObserver instances observe a single file in the filesystem and notify via
 * a {@link FileContentChangeNotifier} if any changes to the file's content
 * occur or if the file gets deleted.
 *
 * TODO I have yet to find a simple way to unit test this class that does not
 * involve working with the actual file system...
 */
public final class FileObserver {

  private FileContentChangeNotifier changeNotifier;
  private WatchService watchService;
  private WatchKey currentlyRegisteredWatchKey;
  private Thread currentlyRunningThread;
  private Path file;
  private volatile AtomicBoolean shouldContinue;

  public FileObserver()
      throws IOException {
    changeNotifier = new FileContentChangeNotifier();
    var fileSystem = FileSystems.getDefault();
    watchService = fileSystem.newWatchService();
    shouldContinue = new AtomicBoolean();
  }

  public void observe(Path file)
      throws FileNotFoundException, IOException, InterruptedException {
    this.file = file;
    verifyThatFileExists();
    cancelPreviousWatchServiceRegistrationIfPresent();
    registerWatchService();
    startNewWatchServiceThread();
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

  private void startNewWatchServiceThread() {
    stopOldThread();
    startNewThread();
  }

  private void stopOldThread() {
    shouldContinue.set(false);
  }

  private void startNewThread() {
    currentlyRunningThread = new Thread(() -> {
      try {
        runWatchService();
      } catch (InterruptedException | IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });
    shouldContinue.set(true);
    currentlyRunningThread.start();
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

  public void subscribeToChangeNotifier(
      Object subscriber, Consumer<String> callback) {
    changeNotifier.subscribe(subscriber, callback);
  }

  public void unsubscribeFromChangeNotifier(Object subscriber) {
    changeNotifier.unsubscribe(subscriber);
  }

}

