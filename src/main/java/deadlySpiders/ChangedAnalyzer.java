package deadlySpiders;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;
import org.apache.lucene.analysis.miscellaneous.TrimFilterFactory;
import org.apache.lucene.analysis.standard.ClassicFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import java.io.IOException;

public class ChangedAnalyzer {

    public static Analyzer getAnalyzer() throws IOException {
        return CustomAnalyzer.builder().withTokenizer(StandardTokenizerFactory.class)
                .addTokenFilter(ClassicFilterFactory.class)
                .addTokenFilter(TrimFilterFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(StopFilterFactory.class)
                .addTokenFilter(PorterStemFilterFactory.class)
                .addTokenFilter(EnglishPossessiveFilterFactory.class)
                .build();
    }
}
