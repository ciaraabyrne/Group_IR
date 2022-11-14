package org.example;
import java.io.*;
import java.nio.charset.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class LATimes {
    private static final String INDEX_DIRECTORY = "../index";

    //    public static void listFilesForFolder(final File folder) { //https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
//      File[] allfiles ;
//        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
//            if (fileEntry.isDirectory()) {
//                listFilesForFolder(fileEntry);
//
//            } else {
//                System.out.println(fileEntry.getName());
//                allfiles = fileEntry;
//            }
//        }
//    }
    public enum lat_Tags {
        DOCNO("<DOCNO>"), DOCID("<DOCID>"), CORRECTION("<CORRECTION>"),
        CORRECTION_DATE("<CORRECTION-DATE>"), TEXT("<TEXT>"),DATE("<DATE>"),DATELINE("<DATELINE>"),  HEADLINE("<HEADLINE>"),   SECTION("<SECTION>"),
        BYLINE("<BYLINE>"),LENGTH("<LENGTH>"), GRAPHIC("<GRAPHIC>"), SUBJECT("<SUBJECT>"), TYPE("<TYPE>"), CELLRULE("<CELLRULE>"),
        ROWRULE("<ROWRULE>"), TABLE("<TABLE ...>"), TABLECELL("<TABLECELL ...>"), TABLEROW("<TABLEROW>");

        String tag;

        lat_Tags(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    public static void indexInput(String[] Latimes) throws IOException {
        if (Latimes.length <= 0) {
            System.out.println("Expected corpus as input");
            System.exit(1);
        }
        Analyzer analyzer = new StandardAnalyzer(); // slightly better results than standard
        ArrayList<Document> documents = new ArrayList<Document>(); // all documents in file
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY)); // Open the directory that contains the search index

        IndexWriterConfig config = new IndexWriterConfig(analyzer); // configure index writer with analyser
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(directory, config); // new index writer with configuration and index directory
        System.out.println(Arrays.toString(Latimes));
        for (String arg : Latimes) {

            System.out.printf("Indexing \"%s\"\n", arg);
            final File folder = new File(arg);
//            listFilesForFolder(folder);
            File[] listOfFiles = folder.listFiles();
            assert listOfFiles != null;
            for (File file : listOfFiles) {
                if (!file.getName().equals("readchg.txt") && !file.getName().equals("readmela.txt")) {
                    if (file.isFile()) {

                        String content = Files.readString(Paths.get(file.toURI()));
                        org.jsoup.nodes.Document article = Jsoup.parse(content);
                        Elements list = article.getElementsByTag("doc");
                        System.out.println(file.getName());
                        for (Element doc : list) {
                            Document lucdoc = new Document();

                            if (doc.getElementsByTag(lat_Tags.DOCNO.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.DOCNO.name())));
                                lucdoc.add(new TextField("DOCNO", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.DOCID.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.DOCID.name())));
                                lucdoc.add(new TextField("DOCID", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.DATE.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.DATE.name())));
                                lucdoc.add(new TextField("DATE", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.HEADLINE.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.HEADLINE.name())));
                                lucdoc.add(new TextField("HEADLINE", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.SECTION.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.SECTION.name())));
                                lucdoc.add(new TextField("SECTION", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.TEXT.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.TEXT.name())));
                                lucdoc.add(new TextField("TEXT", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.BYLINE.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.BYLINE.name())));
                                lucdoc.add(new TextField("BYLINE", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.GRAPHIC.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.GRAPHIC.name())));
                                lucdoc.add(new TextField("GRAPHIC", x , Field.Store.YES));
                            }
                            if (doc.getElementsByTag(lat_Tags.TYPE.name()) != null) {
                                String x = String.valueOf((doc.getElementsByTag(lat_Tags.TYPE.name())));
                                lucdoc.add(new TextField("TYPE", x , Field.Store.YES));
                            }

                        }
                        //        String[] tokens = content.split("(?=\\s*<DOC>\\s*)");
                        //                   for (String i : tokens) {
//                        if(i.length()>2) {
//
//                            String[] splits = i.split("(?=\\s*<[TABW]>\\s*)");
//                        }
                        //                 }

                    }
                }

//            String content = new String(Files.readAllBytes(Paths.get(arg)));

//            String[] tokens = content.split("(?=\\s*<DOC>\\s*)");
//            for (String i : tokens) {
//                String[] splits = i.split("(?=\\s*.[TABW]\\s*)"); // split at every TABW and include these in the split
                //          System.out.println(arg);
                //             System.out.println(content);

//                for (String x : splits) {
//
//                }
//                // Add the file to our linked list
//                documents.add(doc);
//            }

            }
            iwriter.addDocuments(documents); // write documents to index directory
            iwriter.close();
            directory.close();
        }

    }
}
