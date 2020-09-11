package com.signature.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer {

    /*

      @author www.codejava.net

     */

    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputFile Path of the original image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException give exception if files not found.
     */
    public static File resize(File inputFile, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        BufferedImage inputImage = ImageIO.read(inputFile);
        String outputImagePath = System.getProperty("user.home") + File.separator + ".contactData" + File.separator + "images" + File.separator + "TempOutputImage.jpg";
        File outputFile = new File(outputImagePath);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, outputFile);

        return outputFile;
    }

    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputFile Path of the original image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException give exception if files not found.
     */
    public static File resize(File inputFile, double percent) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        return resize(inputFile, scaledWidth, scaledHeight);
    }
}
