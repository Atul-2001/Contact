package com.signature.util;

import com.signature.ui.Main;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageReadWrite {

    private static final String IMAGE_HOME = System.getProperty("user.home") + File.separator + ".contactData" + File.separator + "images" + File.separator;

    public static String readImageFile(InputStream input, String filename) {
//         write binary stream into file
        String filepath = IMAGE_HOME + filename + ".jpg";
        FileOutputStream fos;
        try {
            File file = new File(filepath);
            fos = new FileOutputStream(file);

//            System.out.println("Writing BLOB to file " + file.getAbsolutePath());
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {
                fos.write(buffer);
            }
            fos.close();
//            System.out.println("Read : " + filepath + " Size : " + FileSize.getSizeInKB(filepath) + "KB " + FileSize.getSizeInMB(filepath) + "MB");
            return file.toURI().toString();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static File createImage(File inputImageFile) throws IOException {
        long size = FileSize.getSizeInKB(inputImageFile.toString());
        if (size > 0 && size <= 20) {
            return ImageResizer.resize(inputImageFile, 1.0);
        } else if (size > 20 && size <= 512) {
            return ImageResizer.resize(inputImageFile, 0.6);
        } else if (size > 512 && size <= 1024) {
            return ImageResizer.resize(inputImageFile, 0.4);
        } else if (size > 1024) {
            return ImageResizer.resize(inputImageFile, 0.1);
        } else {
            return null;
        }
    }

    public static byte[] writeImageFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            String testPath = (new URI(file)).normalize().getPath();
            if (Main.isWindows) {
                testPath = testPath.substring(1);
            }
            File filepath = new File(testPath.replaceAll("%20", " "));
//            System.out.println("Write : " + filepath.toString());
            Path path = FileSystems.getDefault().getPath(filepath.toString());
            if (Files.exists(path)) {

                if (Files.isRegularFile(path)) {

                    File formattedImage = createImage(filepath);
                    if (formattedImage != null) {

                        FileInputStream fis = new FileInputStream(formattedImage);
                        byte[] buffer = new byte[1024];
                        bos = new ByteArrayOutputStream();
                        for (int len; (len = fis.read(buffer)) != -1; ) {
                            bos.write(buffer, 0, len);
                        }
                        fis.close();
                    }
                    assert formattedImage != null;
                    Files.deleteIfExists(FileSystems.getDefault().getPath(formattedImage.toString()));
                }
            }
        } catch (IOException | NullPointerException | URISyntaxException e) {
            System.err.println(e.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }
}