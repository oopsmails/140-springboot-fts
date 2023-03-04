package com.oopsmails.lucenesearch.common;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.jupiter.api.Test;

public class LuceneAnalyzerTest {

    @Test
    public void testAnalyzer() throws Exception {
        Analyzer analyzer = new WhitespaceAnalyzer();
        String text = "The quick brown fox jumped over the lazy dog";

        TokenStream tokenStream = analyzer.tokenStream(null, text);
        tokenStream = new LowerCaseFilter(tokenStream);

        CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println(termAttribute.toString());
        }

        tokenStream.end();
        tokenStream.close();
    }

}
