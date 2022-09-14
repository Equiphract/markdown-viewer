package me.equiphract.markdownviewer.model.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.equiphract.markdownviewer.model.io.FileContentReader;

class SimpleStyleProviderTest {

  private String stylesDirectory;
  private FileContentReader fileContentReader;
  private SimpleStyleProvider provider;

  @BeforeEach
  void setUp() {
    stylesDirectory = "/path/to/somewhere/";
    fileContentReader = mock(FileContentReader.class);
    provider = new SimpleStyleProvider(stylesDirectory, fileContentReader);
  }

  @Test
  void SimpleStyleProvider_givenNullStylesDirectory_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> new SimpleStyleProvider(null, fileContentReader));
  }

  @Test
  void SimpleStyleProvider_givenNullFileContentReader_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> new SimpleStyleProvider(stylesDirectory, null));
  }

  @Test
  void getStyle_givenNull_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> provider.getStyle(null));
  }

  @Test
  void getStyle_givenExistingStyle_returnsStyle() throws IOException {
    var expectedStyle = """
      p {
        color: red;
      }
      """;

    when(fileContentReader.read(any(Path.class))).thenReturn(expectedStyle);

    String actualStyle = provider.getStyle("Red_Text");

    assertEquals(expectedStyle, actualStyle);
  }

}

