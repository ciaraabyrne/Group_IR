package deadlySpiders;

import deadlySpiders.Parsers.FBIS;
import deadlySpiders.Parsers.FederalReg;
import deadlySpiders.Parsers.FinancialTimes;
import deadlySpiders.Parsers.LATimes;
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

    public static void index(String indexDir, String FBISPath, String laTimesPath, String fedRegPath, String financialTimesPath) throws IOException {
        System.out.println("Building index...");
        Analyzer analyzer = new ChangedAnalyzer();
        Directory index_dir = FSDirectory.open(Paths.get(indexDir));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(index_dir, config);
        int docCount = 0;
        List<Document> parsedDocs = FBIS.getFbisFiles("files/fbis");
        docCount += parsedDocs.size();
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        parsedDocs = LATimes.parseLaTimes("files/latimes");
        docCount += parsedDocs.size();
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        parsedDocs = FederalReg.parseFedRegisterDocs("files/fr94");
        docCount += parsedDocs.size();
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        parsedDocs = FinancialTimes.parseFinancialTimes("files/ft");
        docCount += parsedDocs.size();
        System.out.println("Doc count=" + docCount);
        iwriter.addDocuments(parsedDocs);
        iwriter.commit();
        iwriter.close();
    }


}
