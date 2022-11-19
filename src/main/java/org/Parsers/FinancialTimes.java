package org.Parsers;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FinancialTimes {


    public static List<Document> parseFinancialTimes(String FTPath) throws IOException {

        System.out.println("Parsing Financial Times");

        List<Document> parsedDocs = new ArrayList<>();
        File[] directories = new File(FTPath).listFiles(File::isDirectory);
        for (File directory : directories) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (!file.getName().equals("readfrcg") && !file.getName().equals("readmeft")) {
                    org.jsoup.nodes.Document article = Jsoup.parse(file, null, "");
                    Elements docs = article.select("DOC");
                    for (Element doc : docs) {
                        Document document = new Document();
                        document.add(new StringField("DOCNO", doc.getElementsByTag("DOCNO").text(), Field.Store.YES));
                        document.add(new TextField("PROFILE", doc.getElementsByTag("PROFILE").text(), Field.Store.YES));
                        document.add(new TextField("DATE", doc.getElementsByTag("DATE").text(), Field.Store.YES));
                        document.add(new TextField("HEADLINE", doc.getElementsByTag("HEADLINE").text(), Field.Store.YES));
                        document.add(new TextField("BYLINE", doc.getElementsByTag("BYLINE").text(), Field.Store.YES));
                        document.add(new TextField("DATELINE", doc.getElementsByTag("DATELINE").text(), Field.Store.YES));
                        document.add(new TextField("TEXT", doc.getElementsByTag("TEXT").text(), Field.Store.YES));
                        document.add(new TextField("PUB", doc.getElementsByTag("PUB").text(), Field.Store.YES));
                        document.add(new TextField("PAGE", doc.getElementsByTag("PAGE").text(), Field.Store.YES));
                        parsedDocs.add(document);
                    }
                }

            }
        }
        return parsedDocs;
    }
}
