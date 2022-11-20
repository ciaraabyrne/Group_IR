package deadlySpiders;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

import static deadlySpiders.Index.index;
import static deadlySpiders.Query.runQueries;

public class Main {


    public static void main(String[] args) throws IOException, ParseException {
        index("index", "files/fbis", "files/latimes", "files/fr94", "files/ft");
        runQueries();
    }

}