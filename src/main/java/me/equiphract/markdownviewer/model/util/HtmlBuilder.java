package me.equiphract.markdownviewer.model.util;

public final class HtmlBuilder {

  private static final String HTML_SKELETON = """
    <!DOCTYPE html>
    <html>
      <head>
        <base href="file://%s/">
      </head>
      <body>
        %s
      </body>
    </html>
    """;

  public String buildHtml(String baseUrl, String bodyContent) {
    throwIfArgumentsContainNull(baseUrl, bodyContent);

    String sanitizedBaseUrl = sanitizeBaseUrl(baseUrl);
    return HTML_SKELETON.formatted(sanitizedBaseUrl, bodyContent);
  }

  private void throwIfArgumentsContainNull(String baseUrl, String bodyContent) {
    if (baseUrl == null || bodyContent == null) {
      throw new IllegalArgumentException("Null arguments are not allowed.");
    }
  }

  private String sanitizeBaseUrl(String baseUrl) {
    if (baseUrl.endsWith("/")) {
      return baseUrl.substring(0, baseUrl.length() - 1);
    }

    return baseUrl;
  }

}

