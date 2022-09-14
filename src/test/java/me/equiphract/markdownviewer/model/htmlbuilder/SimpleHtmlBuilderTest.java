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
  void build_givenBaseUrlWithEndingSlash_returnsHtmlWithBaseUrlWithEndingSlash() {
    var baseUrl = "/path/to/somewhere/";
    var bodyContent = "";

    String constructedHtml = htmlBuilder.build(baseUrl, bodyContent);
    Document document = Jsoup.parse(constructedHtml);

    String actualBaseUrl = document.select("head base").attr("href");

    boolean hasEndingSlash = actualBaseUrl.endsWith(baseUrl);
    var failureMessage = "<%s> should end with <%s> but it does not."
      .formatted(actualBaseUrl, baseUrl);

    assertTrue(hasEndingSlash, failureMessage);
  }

  @Test
  void build_givenBaseUrlWithoutEndingSlash_returnsHtmlWithBaseUrlWithEndingSlash() {
    var baseUrl = "/path/to/somewhere";
    var bodyContent = "";
    var expectedBaseUrl = baseUrl + "/";

    String constructedHtml = htmlBuilder.build(baseUrl, bodyContent);
    Document document = Jsoup.parse(constructedHtml);

    String actualBaseUrl = document.select("head base").attr("href");

    boolean hasEndingSlash = actualBaseUrl.endsWith(expectedBaseUrl);
    var failureMessage = "<%s> should end with <%s> but it does not."
      .formatted(actualBaseUrl, expectedBaseUrl);

    assertTrue(hasEndingSlash, failureMessage);
  }

  @Test
  void build_givenBaseUrl_returnsHtmlWithBaseUrlWithFileSchemePrefix() {
    var baseUrl = "/path/to/somewhere";
    var fileScheme = "file://";
    var bodyContent = "";

    String constructedHtml = htmlBuilder.build(baseUrl, bodyContent);
    Document document = Jsoup.parse(constructedHtml);

    String actualBaseUrl = document.select("head base").attr("href");

    boolean hasFileSchemePrefix = actualBaseUrl.startsWith(fileScheme);
    var failureMessage = "<%s> should end with <%s> but it does not."
      .formatted(actualBaseUrl, baseUrl);

    assertTrue(hasFileSchemePrefix, failureMessage);
  }

  @Test
  void build_givenBodyContent_returnsHtmlWithCorrectBodyContent() {
    var baseUrl = "";
    var bodyContent = "<p>Do not panic, this is just a test!</p>";

    String constructedHtml = htmlBuilder.build(baseUrl, bodyContent);
    Document document = Jsoup.parse(constructedHtml);

    assertEquals(bodyContent, document.select("html body p").toString());
  }

  @Test
  void build_givenNullBaseUrl_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> htmlBuilder.build(null, ""));
  }

  @Test
  void build_givenNullBodyContent_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> htmlBuilder.build("", null));
  }

  // @Test
  // void build_havingBuiltNoNewDocument_throwsHtmlBuildException() {
  //   assertThrows(DocumentBuildException.class, () -> htmlBuilder.build());
  // }

}

