package com.kennycason.kumo.placement;

import java.util.HashSet;
import java.util.Set;

import com.kennycason.kumo.Word;

/**
 * Created by kenny on 2/21/16.
 */
public class LinearWordPlacer implements RectangleWordPlacer {
  private final Set<Word> placedWords = new HashSet<>();

  @Override
  public void reset() {
    placedWords.clear();
  }

  @Override
  public boolean place(final Word word) {
    for (final Word placeWord : this.placedWords) {
      if (placeWord.collide(word)) {
        return false;
      }
    }
    placedWords.add(word);
    return true;
  }

}
