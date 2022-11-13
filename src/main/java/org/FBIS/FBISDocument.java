package org.FBIS;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.LinkedHashMap;

@JacksonXmlRootElement(localName = "DOC")
public class FBISDocument {
    @JacksonXmlProperty(localName = "DOCNO")
    private String docNo;
    @JacksonXmlProperty(localName = "TEXT")
    private String  text;
    @JacksonXmlProperty(localName = "HEADER")
    private LinkedHashMap header;

    public String docNo() {
        return docNo;
    }

    public String text() {
        return text;
    }

    public LinkedHashMap header() {
        return header;
    }
}
