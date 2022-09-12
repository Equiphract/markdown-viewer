package me.equiphract.markdownviewer.model.htmlbuilder;

import java.util.LinkedList;
import java.util.List;

final class SimpleHtmlElement implements HtmlElement {

  private static final String NULL_ARGUMENT_EXCEPTION_MESSAGE =
    "Null values are not permitted.";

  private final String elementName;
  private final List<String> attributes;
  private final List<HtmlElement> children;

  public SimpleHtmlElement(String elementName) {
    this.elementName = elementName;
    attributes = new LinkedList<>();
    children = new LinkedList<>();
  }

  @Override
  public void addAttribute(String name, String value)
      throws IllegalArgumentException {

    if (name == null || value == null) {
      throw new IllegalArgumentException(NULL_ARGUMENT_EXCEPTION_MESSAGE);
    }

    attributes.add(" %s=\"%s\"".formatted(name, value));
  }

  @Override
  public void addChild(HtmlElement child) throws IllegalArgumentException {
    if (child == null) {
      throw new IllegalArgumentException(NULL_ARGUMENT_EXCEPTION_MESSAGE);
    }

    children.add(child);
  }

  @Override
  public String toString() {
    var stringBuilder = new StringBuilder();

    appendOpeningTag(stringBuilder);
    appendChildren(stringBuilder);
    appendClosingTag(stringBuilder);

    return stringBuilder.toString();
  }

  private void appendOpeningTag(StringBuilder stringBuilder) {
    stringBuilder.append("<");
    stringBuilder.append(elementName);
    attributes.forEach(a -> stringBuilder.append(a));
    stringBuilder.append(">");
  }

  private void appendChildren(StringBuilder stringBuilder) {
    children.forEach(c -> stringBuilder.append(c.toString()));
  }

  private void appendClosingTag(StringBuilder stringBuilder) {
    stringBuilder.append("</");
    stringBuilder.append(elementName);
    stringBuilder.append(">");
  }

}

