package me.equiphract.markdownviewer.model.htmlbuilder;

interface HtmlElement {

  void addAttribute(String name, String value) throws IllegalArgumentException;
  void addChild(HtmlElement child) throws IllegalArgumentException;

}

