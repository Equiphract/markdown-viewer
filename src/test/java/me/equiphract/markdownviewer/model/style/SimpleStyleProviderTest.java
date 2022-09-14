package me.equiphract.markdownviewer.model.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class SimpleStyleProviderTest {

  private SimpleStyleProvider provider = new SimpleStyleProvider();

  @Test
  void getStyle_givenNull_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> provider.getStyle(null));
  }

  @Test
  void getStyle_givenExistingStyle_returnsStyle() throws IOException {
    var firstLine = "p {";
    var secondLine = "  color: red;";
    var thirdLine = "}";
    var expectedStyle = """
      %s
      %s
      %s
      """.formatted(firstLine, secondLine, thirdLine);

    BufferedReader reader = mock(BufferedReader.class);
    when(reader.readLine()).thenReturn(firstLine, secondLine, thirdLine, null);

    String actualStyle = provider.getStyle(reader);

    assertEquals(expectedStyle, actualStyle);
  }

}

