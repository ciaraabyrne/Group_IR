package deadlySpiders;


import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;

import org.apache.lucene.analysis.core.StopFilter;

import org.apache.lucene.analysis.en.*;

import org.apache.lucene.analysis.standard.ClassicTokenizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ChangedAnalyzer extends StopwordAnalyzerBase {


    @Override
    protected TokenStreamComponents createComponents(String s) {
        Tokenizer source = new ClassicTokenizer();
        TokenStream tokenStream = new LowerCaseFilter(source);
        tokenStream = new EnglishPossessiveFilter(tokenStream);
        tokenStream = new EnglishMinimalStemFilter(tokenStream);
        tokenStream = new KStemFilter(tokenStream);
        tokenStream = new PorterStemFilter(tokenStream);
        String[] stop_words = getEnglishStopWordList();
//        TokenStream stopSet = StopFilter.makeStopSet(stop_words, true);
        tokenStream = new StopFilter(tokenStream, StopFilter.makeStopSet(stop_words, true));
        return new TokenStreamComponents(source, tokenStream);
    }


    public String[] getEnglishStopWordList() {
        try {
            String content = Files.readString(Paths.get("stopwords.tx"));
            String[] stopwords = content.split("\n");
            return stopwords;
        } catch (IOException e){
            return new String[]{};
        }
    }
}