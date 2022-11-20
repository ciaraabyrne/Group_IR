package deadlySpiders.Parsers;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;



import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class LATimes {
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


    public static List<Document> parseLaTimes(String LaTimesPath) throws IOException {

        System.out.println("Parsing LaTimes");


        ArrayList<Document> documents = new ArrayList<Document>(); // all documents in file

        for (Path file : Files.newDirectoryStream(Paths.get(LaTimesPath))) {
            if (!file.getFileName().toString().equals("readchg.txt") && !file.getFileName().toString().equals("readmela.txt")) {
                if (Files.isRegularFile(file)) {

                    String content = Files.readString(file, StandardCharsets.ISO_8859_1);
                    org.jsoup.nodes.Document article = Jsoup.parse(content);
                    Elements list = article.getElementsByTag("doc");
                    for (Element doc : list) {
                        Document lucdoc = new Document();
                        if (doc.getElementsByTag(lat_Tags.DOCNO.name()) != null) {
                            String x = String.valueOf((doc.getElementsByTag(lat_Tags.DOCNO.name())));
                            lucdoc.add(new StringField("DOCNO", x, Field.Store.YES));
                        }
                        if (doc.getElementsByTag(lat_Tags.DOCID.name()) != null) {
                            String x = String.valueOf((doc.getElementsByTag(lat_Tags.DOCID.name())));
                            lucdoc.add(new TextField("DOCID", x, Field.Store.YES));
                        }
                        if (doc.getElementsByTag(lat_Tags.HEADLINE.name()) != null) {
                            String x = String.valueOf((doc.getElementsByTag(lat_Tags.HEADLINE.name())));
                            lucdoc.add(new TextField("HEADLINE", x, Field.Store.YES));
                        }
                        if (doc.getElementsByTag(lat_Tags.TEXT.name()) != null) {
                            String x = String.valueOf((doc.getElementsByTag(lat_Tags.TEXT.name())));
                            lucdoc.add(new TextField("TEXT", x, Field.Store.YES));
                        }
                        if (doc.getElementsByTag(lat_Tags.DATE.name()) != null) {
                            String x = String.valueOf((doc.getElementsByTag(lat_Tags.DATE.name())));
                            lucdoc.add(new TextField("DATE", x, Field.Store.YES));
                        }
                        documents.add(lucdoc);
                    }

                }
            }
        }
        return documents;
    }
}

