package com.kennycason.kumo;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runners.Parameterized;

/**
 *
 * @author joerg1985
 */
//@RunWith(Parameterized.class)
public class RotateWordTest {

  @Parameterized.Parameters
  public static Iterable<Object[]> data() {
    final List<Object[]> params = new ArrayList<>();
    final String text = "rotating text is great!!";

    for (int i = 20; i < 100; i += 10) {
      params.add(new Object[] { new Font("Arial", 0, i), text.toLowerCase() });
      params.add(new Object[] { new Font("Arial", 0, i), text });
      params.add(new Object[] { new Font("Arial", 0, i), text.toUpperCase() });
    }

    return params;
  }

  private final Font _font;
  private final String _text;

  public RotateWordTest(Font font, String text) {
    _font = font;
    _text = text;
  }

  @Ignore
  public void checkRotatedTextIsNotCropped() throws IOException {
    final BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    final Graphics2D graphics = image.createGraphics();

    // set the rendering hint here to ensure the font metrics are correct
    graphics.setRenderingHints(Word.getRenderingHints());
    final FontMetrics fontMetrics = graphics.getFontMetrics(_font);

    for (int angle = 0; angle < 360; angle += 5) {
      final Word word = new Word(_text, Color.red, fontMetrics, null, Math.toRadians(angle));

      final BufferedImage rendered = word.getBufferedImage();

      final int width = rendered.getWidth();
      final int height = rendered.getHeight();

      try {
        for (int y = 0; y < height; y++) {
          Assert.assertTrue("text doesn't touch the outer left line", nearlyTransparent(rendered.getRGB(0, y)));
          Assert.assertTrue("text doesn't touch the outer right line",
              nearlyTransparent(rendered.getRGB(width - 1, y)));
        }

        for (int x = 0; x < width; x++) {
          Assert.assertTrue("text doesn't touch the top line", nearlyTransparent(rendered.getRGB(x, 0)));
          Assert.assertTrue("text doesn't touch the bottom line", nearlyTransparent(rendered.getRGB(x, height - 1)));
        }
      } catch (final AssertionError e) {
        final File file = new File("output/FailedRotateWordTest_" + System.currentTimeMillis() + ".png");
        ImageIO.write(rendered, "png", file);

        throw e;
      }
    }
  }

  private static boolean nearlyTransparent(int argb) {
    return (argb & 0xFF000000) < 0x40000000;
  }
}
