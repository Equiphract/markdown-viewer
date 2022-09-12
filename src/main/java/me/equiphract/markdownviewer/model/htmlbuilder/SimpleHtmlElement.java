package me.equiphract.markdownviewer.model.htmlbuilder;

import java.util.LinkedList;
import java.util.List;

final class SimpleHtmlElement implements HtmlElement {

  private final String tagName;
  private final List<String> attributes;

  public SimpleHtmlElement(String tagName) {
    this.tagName = tagName;
    attributes = new LinkedList<>();
  }

  @Override
  public void addAttribute(String name, String value) {
    attributes.add(" %s=\"%s\"".formatted(name, value));
  }

  @Override
  public String toString() {
    var stringBuilder = new StringBuilder();
    stringBuilder.append("<");
    stringBuilder.append(tagName);
    attributes.forEach(a -> stringBuilder.append(a));
    stringBuilder.append(">");

    stringBuilder.append("</");
    stringBuilder.append(tagName);
    stringBuilder.append(">");

    return stringBuilder.toString();
  }

}

