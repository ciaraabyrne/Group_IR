package com.assignment1.luceneproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;  
 
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;

public class A2Indexer {
	public static void main(String[] args)
    {
        //Input folder
		String  docsPath = "Files_A2/ft";
		String indexPath = "index_A2";
		Analyzer analyzer = new StandardAnalyzer();
    	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
    	iwc.setOpenMode(OpenMode.CREATE);
    	try
        {
        	System.out.print("Started Indexing....\n");
        	Directory dir = FSDirectory.open( Paths.get(indexPath) ); 
        	IndexWriter writer = new IndexWriter(dir, iwc);
            File file = new File(docsPath);       
    		Collection<File> files = FileUtils.listFiles(file, null, true); 
    		int count=0;
    		int doccnt=0;
    		for(File file1 : files){
    			
    			if(!file1.getName().contains("read")) {
    			count++;	
    		    System.out.println("Started Indexing...."+"File:"+file1.getName()+" Path:"+file1.toPath());
    		    String content = Files.readString(file1.toPath(), StandardCharsets.ISO_8859_1);
                org.jsoup.nodes.Document parsed = Jsoup.parse(content);
                Elements docs = parsed.select("DOC");
                for (Element doc : docs) {
                    Document document = new Document();
                    document.add(new StringField("DOCNO", doc.getElementsByTag("DOCNO").text(), Field.Store.YES));
                    document.add(new StringField("PROFILE", doc.getElementsByTag("PROFILE").text(), Field.Store.YES));
                    document.add(new StringField("DATE", doc.getElementsByTag("DATE").text(), Field.Store.YES));
                    document.add(new StringField("HEADLINE", doc.getElementsByTag("HEADLINE").text(), Field.Store.YES));
                    document.add(new StringField("BYLINE", doc.getElementsByTag("BYLINE").text(), Field.Store.YES));
                    document.add(new StringField("DATELINE", doc.getElementsByTag("DATELINE").text(), Field.Store.YES));
                    document.add(new TextField("TEXT", doc.getElementsByTag("TEXT").text(), Field.Store.YES));
                    document.add(new TextField("PUB", doc.getElementsByTag("PUB").text(), Field.Store.YES));
                    document.add(new TextField("PAGE", doc.getElementsByTag("PAGE").text(), Field.Store.YES));
                    System.out.println(document);
                    writer.addDocument(document);
                    doccnt++;
                }
    		    
    			}
    		}
    		System.out.println("Completed Indexing! \nFiles:"+count+" Documents:"+doccnt);
    		writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
