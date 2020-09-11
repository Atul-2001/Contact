package com.signature.util;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSize {

    private static String file = null;

    private static boolean isValidFile(String file) {
        FileSize.file = file.replaceAll("%20", " ");
        Path filePath = FileSystems.getDefault().getPath(FileSize.file);
        if (Files.exists(filePath)) {
            return Files.isRegularFile(filePath);
        }
        return false;
    }

    public static long getSizeInKB(String file) {
        if (isValidFile(file)) {
            long sizeInBytes = (new File(FileSize.file)).length();
            if (sizeInBytes > 0) {
                return (sizeInBytes/1024);
            }
        }
        return 0;
    }

    public static long getSizeInMB(String file) {
        if (isValidFile(file)) {
            long sizeInBytes = (new File(FileSize.file)).length();
            if (sizeInBytes > 0) {
                return (sizeInBytes/1024)/1024;
            }
        }

        return 0;
    }

    public static long getSizeInGB(String file) {
        if (isValidFile(file)) {
            long sizeInBytes = (new File(FileSize.file)).length();
            if (sizeInBytes > 0) {
                return ((sizeInBytes/1024)/1024)/1024;
            }
        }

        return 0;
    }
}
