package me.equiphract.markdownviewer.model.htmlbuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

class SimpleHtmlBuilderTest {

  private SimpleHtmlBuilder htmlBuilder = new SimpleHtmlBuilder();

  @Test
  void buildHtml_givenBasePathWithEndingSlash_returnsHtmlWithBaseUrlWithEndingSlash() {
    var baseUrl = "/path/to/somewhere/";
    var bodyContent = "";

    String constructedHtml = htmlBuilder.buildHtml(baseUrl, bodyContent);
    Document document = Jsoup.parse(constructedHtml);

    String actualBaseUrl = document.select("head base").attr("href");
    var failureMessage = "<%s> should end with <%s> but it does not."
      .formatted(actualBaseUrl, baseUrl);
    assertTrue(actualBaseUrl.endsWith(baseUrl), () -> failureMessage);
  }

  @Test
  void buildHtml_givenBasePathWithoutEndingSlash_returnsHtmlWithBaseUrlWithEndingSlash() {
    var baseUrl = "/path/to/somewhere";
    var bodyContent = "";

    String constructedHtml = htmlBuilder.buildHtml(baseUrl, bodyContent);
    Document document = Jsoup.parse(constructedHtml);

    String expectedBaseUrl = baseUrl + "/";
    String actualBaseUrl = document.select("head base").attr("href");

    var failureMessage = "<%s> should end with <%s> but it does not."
      .formatted(actualBaseUrl, baseUrl);
    assertTrue(actualBaseUrl.endsWith(expectedBaseUrl), () -> failureMessage);
  }

  @Test
  void buildHtml_givenBodyContent_returnsHtmlWithCorrectBodyContent() {
    var baseUrl = "";
    var bodyContent = "<p>Do not panic, this is just a test!</p>";

    String constructedHtml = htmlBuilder.buildHtml(baseUrl, bodyContent);
    Document document = Jsoup.parse(constructedHtml);

    assertEquals(bodyContent, document.select("html body p").toString());
  }

  @Test
  void buildHtml_givenNullBaseUrl_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> htmlBuilder.buildHtml(null, ""));
  }

  @Test
  void buildHtml_givenNullBodyContent_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> htmlBuilder.buildHtml("", null));
  }

}

