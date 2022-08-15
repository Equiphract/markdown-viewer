package me.equiphract.markdownviewer.model.markdown;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

public final class MarkdownToHtmlConverter implements MarkdownConverter {
  private final Parser parser;
  private final HtmlRenderer renderer;

  public MarkdownToHtmlConverter() {
    parser = Parser.builder().build();
    renderer = HtmlRenderer.builder().build();
  }

  @Override
  public String convert(String markdown) {
    Node document = parser.parse(markdown);
    return renderer.render(document);
  }

}

