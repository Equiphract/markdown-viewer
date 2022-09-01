package me.equiphract.markdownviewer.model.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileObserverTest {

  private static final String INITIAL_DATA = "Initial Data";
  private static final String UPDATED_DATA = "Updated Data";
  private FileObserver observer;
  private WatchService watchService;
  private Path pathToFile;

  @BeforeEach
  void setUp() throws IOException {
    pathToFile = mock(Path.class);
    watchService = mock(WatchService.class);
    var pathToFileDirectory = mock(Path.class);
    var filesystem = mock(FileSystem.class);
    var filesystemProvider = mock(FileSystemProvider.class);
    var initialInputStream = createNewInputStream(INITIAL_DATA);
    var updatedInputStream = createNewInputStream(UPDATED_DATA);

    when(pathToFile.getParent())
      .thenReturn(pathToFileDirectory);

    when(pathToFile.getFileSystem())
      .thenReturn(filesystem);

    when(filesystem.provider())
      .thenReturn(filesystemProvider);

    // simulates a fake file
    when(filesystemProvider.newInputStream(pathToFile))
      .thenReturn(initialInputStream, updatedInputStream);

    observer = new FileObserver(watchService);
  }

  private InputStream createNewInputStream(String data) {
    return new ByteArrayInputStream(data.getBytes());
  }

  @Test
  void FileObserver_givenNull_throwsIllegalArgumentException()
      throws IOException {

    assertThrows(IllegalArgumentException.class, () -> new FileObserver(null));
  }

  @Test
  void observe_givenNull_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> observer.observe(null));
  }

  @Test
  void observe_onUpdate_notifiesWithUpdate()
      throws FileNotFoundException, IOException, InterruptedException {

    var watchKey = mock(WatchKey.class);
    var watchEvent = mock(WatchEvent.class);

    when(watchEvent.context()).thenReturn(pathToFile);
    when(watchService.take()).thenReturn(watchKey);
    when(watchKey.pollEvents()).thenReturn(List.of());

    // This is horrible, I am sorry.
    observer.subscribe(this, data -> {
      assertEquals(INITIAL_DATA, data);
      when(watchKey.pollEvents()).thenReturn(List.of(watchEvent));
      // `this` refers to the lambda's enclosing context
      observer.unsubscribe(this);
      observer.subscribe(this, this::assertUpdated);
    });

    observer.observe(pathToFile);
  }

  private void assertUpdated(String data) {
    assertEquals(UPDATED_DATA, data);
  }

}

