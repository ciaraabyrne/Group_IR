package deadlySpiders.Parsers;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FederalReg {

    private static List<Document> fedRegisterDocList = new ArrayList<>();

    public static List<Document> parseFedRegisterDocs(String pathToFedRegister) throws IOException {

        System.out.println("Parsing FedRegister");

        File[] directories = new File(pathToFedRegister).listFiles(File::isDirectory);
        String docno,text,title;
        for (File directory : directories) {
            File[] files = directory.listFiles();
            for (File file : files) {
                org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");

                Elements documents = d.select("DOC");

                for (Element document : documents) {

                    title = document.select("DOCTITLE").text();

                    document.select("DOCTITLE").remove();
                    document.select("ADDRESS").remove();
                    document.select("SIGNER").remove();
                    document.select("SIGNJOB").remove();
                    document.select("BILLING").remove();
                    document.select("FRFILING").remove();
                    document.select("DATE").remove();
                    document.select("CRFNO").remove();
                    document.select("RINDOCK").remove();

                    docno = document.select("DOCNO").text();
                    text = document.select("TEXT").text();

                    addFedRegisterDoc(docno, text, title);
                }
            }
        }
        return fedRegisterDocList;
    }

    private static void addFedRegisterDoc(String docno, String text, String title) {
        Document doc = new Document();
        doc.add(new StringField("DOCNO", docno, Field.Store.YES));
        doc.add(new TextField("TEXT", text, Field.Store.YES));
        doc.add(new TextField("HEADLINE", title, Field.Store.YES));
        fedRegisterDocList.add(doc);
    }


}
