package com.kennycason.kumo.wordstart;

import java.awt.Dimension;
import java.awt.Point;

import com.kennycason.kumo.Word;

/**
 * Always returns the Center of the image
 *
 * @author &#64;wolfposd
 */
public class CenterWordStart implements WordStartStrategy {

  @Override
  public Point getStartingPoint(final Dimension dimension, final Word word) {
    final int x = (dimension.width / 2) - (word.getDimension().width / 2);
    final int y = (dimension.height / 2) - (word.getDimension().height / 2);

    return new Point(x, y);
  }

}
