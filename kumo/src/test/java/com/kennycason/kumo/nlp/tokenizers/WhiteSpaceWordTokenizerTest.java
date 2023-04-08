package com.kennycason.kumo.nlp.tokenizers;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.kennycason.kumo.nlp.tokenizer.WhiteSpaceWordTokenizer;
import com.kennycason.kumo.nlp.tokenizer.WordTokenizer;

/**
 * Created by kenny on 2/22/16.
 */
public class WhiteSpaceWordTokenizerTest {

  @Test
  public void singleAndMultipleSpace() {
    final WordTokenizer parser = new WhiteSpaceWordTokenizer();

    final List<String> words = parser.tokenize("hello, i am a     programmer !!  ");
    assertEquals(6, words.size());
    assertEquals("hello,", words.get(0));
    assertEquals("i", words.get(1));
    assertEquals("am", words.get(2));
    assertEquals("a", words.get(3));
    assertEquals("programmer", words.get(4));
    assertEquals("!!", words.get(5));
  }

}
