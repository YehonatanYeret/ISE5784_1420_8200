package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {

    @Test
    void FirstImage() {
        assertDoesNotThrow(() -> {
            Color yellow = new Color(java.awt.Color.YELLOW);
            Color red = new Color(java.awt.Color.RED);
            ImageWriter imageWriter = new ImageWriter("test", 801, 501);
            for (int i = 0; i <= 800; i++) {
                for (int j = 0; j <= 500; j++) {
                    if (i % 50 == 0 || j % 50 == 0) {
                        imageWriter.writePixel(i, j, red);
                    } else {
                        imageWriter.writePixel(i, j, yellow);
                    }
                }
            }
        }, "Failed to create image");
    }
}