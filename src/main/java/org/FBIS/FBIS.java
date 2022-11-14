package org.FBIS;

import java.io.IOException;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FBIS {

    private static final List<String> EXCLUDED_FILES = List.of("readmefb.txt", "readchg.txt");

    public static void main(String[] args) throws  IOException {
        List <org.apache.lucene.document.Document> parsedDocs = getFbisFiles("files/fbis");
        System.out.println(parsedDocs.size());
    }
    public static List<org.apache.lucene.document.Document> getFbisFiles(String fbisFilePath) throws IOException{
        Stream<Path> files = Files.list(Path.of(fbisFilePath));
        return files
                .filter(fileName -> !EXCLUDED_FILES.contains(fileName.getFileName().toString()))
                .map(FBIS::parseFile)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
    private static List<org.apache.lucene.document.Document> parseFile(Path filePath){
        try {
            String content = Files.readString(filePath, StandardCharsets.ISO_8859_1);
            Document parsed = Jsoup.parse(content);
            Elements docs = parsed.select("DOC");
            ArrayList<org.apache.lucene.document.Document> parsedDocs = new ArrayList<>();
            for (Element doc : docs) {
                org.apache.lucene.document.Document parsedDoc = new org.apache.lucene.document.Document();
                parsedDoc.add(new StringField("DOCNO", doc.getElementsByTag("DOCNO").text(), Field.Store.YES));
                parsedDoc.add(new TextField("Text", doc.getElementsByTag("TEXT").text(), Field.Store.YES));
                parsedDoc.add(new TextField("Date", doc.getElementsByTag("DATE1").text(), Field.Store.YES));
                parsedDoc.add(new TextField("Header", doc.getElementsByTag("HEADER").text(), Field.Store.YES));
                parsedDocs.add(parsedDoc);
            }
            return parsedDocs;
        } catch (IOException ex) {
            return Collections.emptyList();
        }
    }



}

