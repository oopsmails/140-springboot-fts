package com.oopsmails.lucenesearch.common;

import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class LuceneTokenizerTest {

    @Test
    public void testTokenizer() throws Exception {
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        String text = "The quick brown fox jumped over the lazy dog";

        tokenizer.setReader(new StringReader(text));
        tokenizer.reset();

        CharTermAttribute termAttribute = tokenizer.addAttribute(CharTermAttribute.class);

        while (tokenizer.incrementToken()) {
            System.out.println(termAttribute.toString());
        }

        tokenizer.end();
        tokenizer.close();
    }

}
