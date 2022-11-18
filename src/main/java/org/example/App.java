package org.example;

import org.apache.lucene.document.Document;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.example.FederalReg.loadFedRegisterDocs;

/**
 * Hello world!
 *
 */
public class App 
{
    private static List<Document> fedRegisterDocs = new ArrayList<>();
    private final static Path currentRelativePath = Paths.get("").toAbsolutePath();
    private final static String absPathToFedRegister = String.format("./src/source/fr94",currentRelativePath);

    public static void main( String[] args ) throws IOException {
//        System.out.println( "Hello World!" );
        loadDocs();

        for(int i=0;i<5;i++) {
            if(args[i].endsWith("latimes")) {
                LATimes.indexInput(args);
            }
        }

        System.out.println(fedRegisterDocs.size());

    }

    private static void loadDocs() throws IOException {
        fedRegisterDocs = loadFedRegisterDocs(absPathToFedRegister);
        System.out.println("FR94 loaded");
    }
}
