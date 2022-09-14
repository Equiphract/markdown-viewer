package me.equiphract.markdownviewer.model.htmlbuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class SimpleHtmlBuilder implements HtmlBuilder {

  private Document document;
  private String baseUrl;
  private String style;
  private String bodyContent;

  @Override
  public String build(String baseUrl, String style, String bodyContent) {
    throwIfArgumentsContainNull(baseUrl, style, bodyContent);

    this.baseUrl = baseUrl;
    this.style = style;
    this.bodyContent = bodyContent;

    addEndingSlashToBaseUrlIfMissing();
    buildDocument();

    return document.toString();
  }

  private void throwIfArgumentsContainNull(
      String baseUrl, String style, String bodyContent) {

    if (baseUrl == null || style == null || bodyContent == null) {
      throw new IllegalArgumentException("Null arguments are not allowed.");
    }
  }

  private void addEndingSlashToBaseUrlIfMissing() {
    var slash = "/";

    if (!baseUrl.endsWith(slash)) {
      baseUrl += slash;
    }
  }

  private void buildDocument() {
    document = Jsoup.parse("<html></html>");
    buildHead();
    buildBody();
  }

  private void buildHead() {
    Element base = document.head().appendElement("base");
    var fileScheme = "file://";
    base.attr("href", fileScheme + baseUrl);
    Element styleElement = document.head().appendElement("style");
    styleElement.appendText(style);
  }

  private void buildBody() {
    document.body().append(bodyContent);
  }

}

