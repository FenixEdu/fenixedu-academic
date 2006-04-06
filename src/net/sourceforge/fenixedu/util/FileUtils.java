/*
 * @(#)FileUtils.java Created on Nov 5, 2004
 * 
 */
package net.sourceforge.fenixedu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author Luis Cruz
 * @author Shezad Anavarali
 * 
 */
public class FileUtils {

    private static int[] fileWriterSynch = new int[0];

    // Cluster safe global unique temporary filename
    private static final String TEMPORARY_FILE_GLOBAL_UNIQUE_NAME = UUID.randomUUID().toString();

    public static String readFile(final String filename) throws IOException {
        final FileReader fileReader = new FileReader(filename);
        char[] buffer = new char[4096];
        final StringBuilder fileContents = new StringBuilder();
        int n = 0;
        while ((n = fileReader.read(buffer)) != -1) {
            fileContents.append(buffer, 0, n);
        }
        fileReader.close();
        return fileContents.toString();
    }

    public static byte[] readFileInBytes(final String filename) throws IOException {
        final File file = new File(filename);
        final FileInputStream fileInputStream = new FileInputStream(file);
        // TODO : fix to only accpet up to max int value.
        final int fileSize = (int) file.length();
        final byte[] buffer = new byte[fileSize];

        if (fileSize > 0) {
            for (int n = 0; (n = fileInputStream.read(buffer, n, fileSize - n)) != -1;) {
            }
        }

        fileInputStream.close();
        return buffer;
    }

    public static void writeFile(final String filename, final String fileContents, final boolean append)
            throws IOException {
        synchronized (fileWriterSynch) {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            final FileWriter fileWriter = new FileWriter(file, append);

            fileWriter.write(fileContents);
            fileWriter.close();
        }
    }

    public static void writeFile(final String filename, final byte[] fileContents, final boolean append)
            throws IOException {
        synchronized (fileWriterSynch) {
            final FileOutputStream fileOutputStream = new FileOutputStream(filename, append);
            fileOutputStream.write(fileContents);
            fileOutputStream.close();
        }
    }

    public static void createDir(final String dir) {
        (new File(dir)).mkdirs();
    }

    public static void deleteDirContents(final String dir) {
        File directory = new File(dir);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {

        final int BUFFER_SIZE = 1024 * 1024;
        final byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            final int numberOfBytesRead = inputStream.read(buffer, 0, BUFFER_SIZE);

            if (numberOfBytesRead == -1) {
                break;
            }

            // write out those same bytes
            outputStream.write(buffer, 0, numberOfBytesRead);
        }
    }

    public static String getTemporaryFileBaseName() {
        return TEMPORARY_FILE_GLOBAL_UNIQUE_NAME;
    }

}