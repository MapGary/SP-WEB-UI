package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Assert {

    public static boolean compareScreenshotsWithTolerance(File screenshot1, File screenshot2, double tolerance) {
        BufferedImage img1 = null;
        BufferedImage img2 = null;

        try {
            img1 = ImageIO.read(screenshot1);
            img2 = ImageIO.read(screenshot2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        int diffPixels = 0;
        int totalPixels = img1.getWidth() * img1.getHeight();

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    diffPixels++;
                }
            }
        }

        double diffPercentage = (double) diffPixels / totalPixels * 100;
        return diffPercentage <= tolerance;
    }
}
