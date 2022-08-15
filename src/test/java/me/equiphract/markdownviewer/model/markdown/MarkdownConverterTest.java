package me.equiphract.markdownviewer.model.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MarkdownToHtmlConverterTest {

  private MarkdownToHtmlConverter converter = new MarkdownToHtmlConverter();

  @Test
  void convert_givenMarkdownHeader_ShouldConvertToHtmlHeader() {
    String actual = converter.convert("# Simple Markdown");
    assertEquals("<h1>Simple Markdown</h1>\n", actual);
  }

  @Test
  void convert_givenMarkdownList_ShouldConvertToHtmlList() {
    var markdownList = """
      - bread
      - cheese
      - beer
    """;

    String expected = """
    <ul>
    <li>bread</li>
    <li>cheese</li>
    <li>beer</li>
    </ul>
    """;
    String actual = converter.convert(markdownList);

    assertEquals(expected, actual);
  }
}

