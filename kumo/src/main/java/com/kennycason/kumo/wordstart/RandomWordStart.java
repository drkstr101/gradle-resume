package com.kennycason.kumo.wordstart;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import com.kennycason.kumo.Word;

/**
 * Always returns a random point in the image as starting point
 *
 * @author &#64;wolfposd
 */
public class RandomWordStart implements WordStartStrategy {

  private static final Random RANDOM = new Random();

  @Override
  public Point getStartingPoint(final Dimension dimension, final Word word) {
    final int startX = RANDOM.nextInt(Math.max(dimension.width - word.getDimension().width, dimension.width));
    final int startY = RANDOM.nextInt(Math.max(dimension.height - word.getDimension().height, dimension.height));

    return new Point(startX, startY);
  }

}
