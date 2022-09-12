package me.equiphract.markdownviewer.model.htmlbuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SimpleHtmlElementTest {
  SimpleHtmlElement element = new SimpleHtmlElement("a");

  @Test
  void toString_returnsCorrectStringRepresentationOfAnHtmlTag() {
    assertEquals("<a></a>", element.toString());
  }

  @Test
  void addAttribute_givenHrefAttribute_addsAttributeToElement() {
    var expectedElement = "<a href=\"www.markdown-viewer.org\"></a>";

    element.addAttribute("href", "www.markdown-viewer.org");
    var actualElement = element.toString();

    assertEquals(expectedElement, actualElement);
  }

  @Test
  void addAttribute_calledTwoTimes_addsTwoAttributesToElement() {
    var expectedElement =
      "<a id=\"home-link\" href=\"www.markdown-viewer.org\"></a>";

    element.addAttribute("id", "home-link");
    element.addAttribute("href", "www.markdown-viewer.org");
    var actualElement = element.toString();

    assertEquals(expectedElement, actualElement);
  }

  @Test
  void addAttribute_givenNullName_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> element.addAttribute(null, ""));
  }

  @Test
  void addAttribute_givenNullValue_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> element.addAttribute("", null));
  }

  @Test
  void addChild_givenHtmlElement_addsHtmlElement() {
    var expectedElement = "<a><img></img></a>";

    element.addChild(new SimpleHtmlElement("img"));
    var actualElement = element.toString();

    assertEquals(expectedElement, actualElement);
  }

  @Test
  void addChild_calledTwoTimes_addsTwoHtmlElements() {
    var expectedElement = "<a><img></img><p></p></a>";

    element.addChild(new SimpleHtmlElement("img"))
      .addChild(new SimpleHtmlElement("p"));
    var actualElement = element.toString();

    assertEquals(expectedElement, actualElement);
  }

  @Test
  void addChild_calledInNestedManner_resultsInNestedHtmlElements() {
    var expectedElement = "<a><p><img></img></p></a>";

    HtmlElement pElement = new SimpleHtmlElement("p");
    HtmlElement imgElement = new SimpleHtmlElement("img");
    element.addChild(pElement.addChild(imgElement));
    var actualElement = element.toString();

    assertEquals(expectedElement, actualElement);
  }

  @Test
  void addChild_givenNullValue_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> element.addChild(null));
  }

}

