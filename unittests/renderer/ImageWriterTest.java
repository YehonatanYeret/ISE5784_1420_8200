package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {
    /**
     * ==== the resolution width of the view plane - =====
     */
    private static final int nX = 801;
    /**
     * ==== the resolution height of the view plane - =====
     */
    private static final int nY = 501;

    /**
     * Yellow is a combination of red &amp; green (for the yellow Square)
     */
    Color yellowColor = new Color(255d, 255d, 0d);
    /**
     * if r=255 the color is red (for the net)
     */
    Color redColor = new Color(255d, 0d, 0d);

    @Test
    void FirstImage() {

        assertDoesNotThrow(() -> {
                ImageWriter imageWriter = new ImageWriter("test", nX, nY);
                //=== running on the view plane===//
                for (int i = 0; i < nX; i++) {
                    for (int j = 0; j < nY; j++) {
                        //=== create the net ===//
                        imageWriter.writePixel(i, j, i % 50 == 0 || j % 50 == 0 ? redColor : yellowColor);
                    }
                }
                imageWriter.writeToImage();
        }, "Failed to create image");
    }
}