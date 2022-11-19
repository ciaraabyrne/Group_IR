package org;

import org.Parsers.LATimes;
import org.Parsers.FBIS;
import org.Parsers.FinancialTimes;
import org.Parsers.FederalReg;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


public class Index
{

    public static void main(String[] args) throws IOException {
        index("index", "files/fbis", "files/latimes", "files/fr94", "files/ft");
    }

    public static void index(String indexDir, String FBISPath, String laTimesPath, String fedRegPath, String financialTimesPath) throws IOException {
        System.out.println("Building index...");
        Analyzer analyzer = new EnglishAnalyzer();
        Directory index_dir = FSDirectory.open(Paths.get(indexDir));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(index_dir, config);
        List<Document> parsedDocs = FBIS.getFbisFiles("files/fbis");
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        parsedDocs = LATimes.parseLaTimes("files/latimes");
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        parsedDocs = FederalReg.parseFedRegisterDocs("files/fr94");
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        parsedDocs = FinancialTimes.parseFinancialTimes("files/ft");
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        iwriter.close();
    }
}
