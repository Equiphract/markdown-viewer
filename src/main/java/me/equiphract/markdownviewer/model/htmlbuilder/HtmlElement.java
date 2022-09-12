package me.equiphract.markdownviewer.model.htmlbuilder;

interface HtmlElement {

  HtmlElement addAttribute(String name, String value)
      throws IllegalArgumentException;
  HtmlElement addChild(HtmlElement child) throws IllegalArgumentException;

}

