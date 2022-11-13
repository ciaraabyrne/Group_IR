package org.FBIS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FBIS {

    private static final List<String> EXCLUDED_FILES = List.of("readmefb.txt", "readchg.txt");

    public static void main(String[] args) throws  IOException {
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String doc = new StringBuilder().append("<FBISFile>\n").append(Files.readString(Paths.get("files/fbis/fb396003"), StandardCharsets.ISO_8859_1)).append("</FBISFile>\n").toString().replaceAll(" .*?=.*?>", ">");
        mapper.readValue(doc, FBISFile.class);
        List <Document> docs = parseFbisFiles("files/fbis");
        System.out.println("test");
    }

    public static List<Document> parseFbisFiles(String fbisPath) throws IOException {
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Stream<Path> files = Files.list(Paths.get(fbisPath));
        List<FBISFile> fixed = files.filter(filePath -> !EXCLUDED_FILES.contains(filePath.getFileName()))
                .map(FBIS::fixFileStructure)
                .map(contents -> readValueSafely(contents, mapper))
                .collect(Collectors.toList());

        return files
                .filter(filePath -> !EXCLUDED_FILES.contains(filePath.getFileName()))
                .map(FBIS::fixFileStructure)
                .map(contents -> readValueSafely(contents, mapper))
                .map(FBIS::parseFbisFile)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<Document> parseFbisFile (FBISFile fileToParse) {
        if(fileToParse.documents != null) {
            return fileToParse.documents.stream()
                    .map(FBIS::parseFbisDocument)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private static Document parseFbisDocument(FBISDocument doc) {
        Document resDoc = new Document();
        resDoc.add(new StringField("index_no", doc.docNo(), Field.Store.YES));
        resDoc.add(new TextField("date", (String) doc.header().get("DATE1"), Field.Store.YES));
        resDoc.add(new TextField("text", doc.text(), Field.Store.YES));
        return resDoc;
    }

    private static String fixFileStructure (Path filePath){
        try {
            String contents = new StringBuilder().append("<FBISFile>\n").append(Files.readString(filePath, StandardCharsets.ISO_8859_1)).append("</FBISFile>\n").toString();
            return contents.replace(" .*?=.*?>", ">");
        } catch (IOException ex) {
            System.out.println(ex);
            return filePath.getFileName().toString();
        }
    }

    private static FBISFile readValueSafely(String contents, XmlMapper mapper) {
        try {
            return mapper.readValue(contents, FBISFile.class);
        } catch (JsonProcessingException ex) {
            System.out.println(ex);
            return new FBISFile();
        }
    }

}

@JacksonXmlRootElement(localName = "FBISFile")
class FBISFile {
    @JacksonXmlProperty(localName = "DOC")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<FBISDocument> documents;
}
