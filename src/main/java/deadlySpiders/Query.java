package deadlySpiders;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Query {

	public static void runQueries() throws ParseException, IOException {
	    System.out.println("Parsing queries...");
		String indexPath = "index";
		String output_path = "Output";
		String queries = "topics";
		Elements topics = null;
		PrintWriter writer = null;
		QueryParserBase parser = null;
		Analyzer analyzer = new ChangedAnalyzer();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new BM25Similarity());
        BufferedReader in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
        writer = new PrintWriter(output_path+"/outputs.txt", "UTF-8");
        HashMap<String, Float> boostedScores = new HashMap<String, Float>();
        boostedScores.put("TEXT", 0.85f);
        boostedScores.put("HEADLINE", 0.15f);
        parser = new MultiFieldQueryParser(
                new String[]{"TEXT", "HEADLINE", "DOCNO"},
                analyzer, boostedScores);
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(new File(queries), "UTF-8", "");
        topics = jsoupDoc.select("top");
        

        int count = 0;
		for (Element topicElement : topics) {
			//System.out.println(topics);
            count++;
			System.out.println("Running query " + count);
            String num = topicElement.getElementsByTag("num").text();
            String title = topicElement.getElementsByTag("title").text();
            String descStr = topicElement.getElementsByTag("desc").text();
            String narrativeStr = topicElement.getElementsByTag("narr").text();
            Pattern numberPattern = Pattern.compile("(\\d+)");
            Matcher numberMatcher = numberPattern.matcher(num);
            String number = "";
            if(numberMatcher.find()) {
                number = numberMatcher.group().trim();
            }

            descStr = descStr.replace("\n"," ");
            Pattern descPattern = Pattern.compile("Description: (.*)Narrative");
            Matcher descMatcher = descPattern.matcher(descStr);
            String desc = "";
            if(descMatcher.find()) {
                desc = descMatcher.group(1).trim();
            }

            String narrative = narrativeStr.replace("\n"," ").replace("Narrative: ","").trim();
            String queryString = title;
            queryString += " " + narrative;
			org.apache.lucene.search.Query query = parser.parse(QueryParser.escape(queryString.trim()));
            queryString = "";
            //System.out.println(query);
            TopDocs results = searcher.search(query,1000);
            ScoreDoc[] hits = results.scoreDocs;
            System.out.println(hits.length);
            for (int i = 0; i < hits.length; i++) {
                ScoreDoc hit = hits[i];
				writer.println(number + " 0 " + searcher.doc(hit.doc).get("DOCNO").replace("<docno>", "").replace("</docno>", "").trim() + " " + i + " " + hits[i].score + " STANDARD");
				//System.out.println(number + " 0 " + searcher.doc(hit.doc).get("DOCNO").replace("<docno>", "").replace("</docno>", "").trim() + " " + i + " " + hits[i].score + " STANDARD");
				
            }
		}
		writer.close();
	}


}

