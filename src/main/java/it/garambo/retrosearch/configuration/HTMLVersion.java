package it.garambo.retrosearch.configuration;

import lombok.Getter;

@Getter
public enum HTMLVersion {
  HTML_2_0("2.0", HTMLConstants.HTML2_0_DOC_TYPE, HTMLConstants.HTML2_0_TAGS),
  HTML_3_2("3.2", HTMLConstants.HTML3_2_DOC_TYPE, HTMLConstants.HTML3_2_TAGS);

  final String id;
  final String docType;
  final String[] allowedTags;

  HTMLVersion(String id, String docType, String[] allowedTags) {
    this.id = id;
    this.docType = docType;
    this.allowedTags = allowedTags;
  }

  public static HTMLVersion getByVersionName(String htmlVersion) {
    if (htmlVersion.equals("2.0")) {
      return HTMLVersion.HTML_2_0;
    }

    return HTMLVersion.HTML_3_2;
  }

  private static class HTMLConstants {

    public static final String HTML2_0_DOC_TYPE =
        "<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">";
    public static final String HTML3_2_DOC_TYPE =
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">";

    public static final String[] HTML2_0_TAGS = {
      "A",
      "ADDRESS",
      "B",
      "BASE",
      "BLOCKQUOTE",
      "BODY",
      "BR",
      "CITE",
      "CODE",
      "DD",
      "DIR",
      "DL",
      "DT",
      "EM",
      "FORM",
      "H1",
      "H2",
      "H3",
      "H4",
      "H5",
      "H6",
      "HEAD",
      "HR",
      "HTML",
      "I",
      "IMG",
      "INPUT",
      "ISINDEX",
      "KBD",
      "LI",
      "LINK",
      "LISTING",
      "MENU",
      "META",
      "NEXTID",
      "OL",
      "OPTION",
      "P",
      "PLAINTEXT",
      "PRE",
      "SAMP",
      "SELECT",
      "STRONG",
      "TEXTAREA",
      "TITLE",
      "TT",
      "UL",
      "VAR",
      "XMP"
    };
    public static final String[] HTML3_2_TAGS = {
      "A",
      "ADDRESS",
      "APPLET",
      "AREA",
      "B",
      "BASE",
      "BASEFONT",
      "BIG",
      "BLOCKQUOTE",
      "BODY",
      "BR",
      "CAPTION",
      "CENTER",
      "CITE",
      "CODE",
      "DD",
      "DFN",
      "DIR",
      "DIV",
      "DL",
      "DT",
      "EM",
      "FONT",
      "FORM",
      "H1",
      "H2",
      "H3",
      "H4",
      "H5",
      "H6",
      "HEAD",
      "HR",
      "HTML",
      "I",
      "IMG",
      "INPUT",
      "ISINDEX",
      "KBD",
      "LI",
      "LINK",
      "LISTING",
      "MAP",
      "MENU",
      "META",
      "OL",
      "OPTION",
      "P",
      "PARAM",
      "PLAINTEXT",
      "PRE",
      "SAMP",
      "SCRIPT",
      "SELECT",
      "SMALL",
      "STRIKE",
      "STRONG",
      "STYLE",
      "SUB",
      "SUP",
      "TABLE",
      "TD",
      "TEXTAREA",
      "TH",
      "TITLE",
      "TR",
      "TT",
      "U",
      "UL",
      "VAR",
      "XMP"
    };
  }
}
