/*
 * @(#)FileUtils.java Created on Nov 5, 2004
 * 
 */
package org.fenixedu.academic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

/**
 * @author Luis Cruz
 * @author Shezad Anavarali
 * 
 */
@Deprecated
public class FileUtils {

    private static int[] fileWriterSynch = new int[0];

    // Cluster safe global unique temporary filename
    private static final String TEMPORARY_FILE_GLOBAL_UNIQUE_NAME_PREFIX = UUID.randomUUID().toString();

    private static final char[] SEPARATOR_CHARS = new char[] { '\\', '/' };

    private static final int BUFFER_SIZE = 1024 * 1024;

    private static final String GZIP_FILE_EXTENSION = ".gz";

    public static String readFile(final String filename) throws IOException {
        return readFile(isGZIP(filename) ? createGZIPReaderWrapper(filename) : createFileReader(filename));
    }

    private static boolean isGZIP(final String filename) {
        return filename.endsWith(GZIP_FILE_EXTENSION);
    }

    private static InputStreamReader createGZIPReaderWrapper(final String filename) throws IOException, FileNotFoundException {
        return new InputStreamReader(new GZIPInputStream(new FileInputStream(filename)));
    }

    private static FileReader createFileReader(final String filename) throws FileNotFoundException {
        return new FileReader(filename);
    }

    public static String readFile(final InputStream inputStream) throws IOException {
        return readFile(new InputStreamReader(inputStream));
    }

    public static String readFile(final InputStreamReader fileReader) throws IOException {
        try {
            char[] buffer = new char[4096];
            final StringBuilder fileContents = new StringBuilder();
            for (int n = 0; (n = fileReader.read(buffer)) != -1; fileContents.append(buffer, 0, n)) {
                ;
            }
            return fileContents.toString();
        } finally {
            fileReader.close();
        }
    }

    public static byte[] readFileInBytes(final String filename) throws IOException {
        final File file = new File(filename);
        final FileInputStream fileInputStream = new FileInputStream(file);
        try {
            // TODO : fix to only accpet up to max int value.
            final int fileSize = (int) file.length();
            final byte[] buffer = new byte[fileSize];

            if (fileSize > 0) {
                for (int n = 0; (n = fileInputStream.read(buffer, n, fileSize - n)) != -1;) {
                    ;
                }
            }
            return buffer;
        } finally {
            fileInputStream.close();
        }

    }

    public static void writeFile(final String filename, final String fileContents, final boolean append) throws IOException {
        synchronized (fileWriterSynch) {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            final FileWriter fileWriter = new FileWriter(file, append);
            try {
                fileWriter.write(fileContents);
            } finally {
                fileWriter.close();
            }
        }
    }

    public static void writeFile(final String filename, final byte[] fileContents, final boolean append) throws IOException {
        synchronized (fileWriterSynch) {
            final FileOutputStream fileOutputStream = new FileOutputStream(filename, append);
            try {
                fileOutputStream.write(fileContents);
            } finally {
                fileOutputStream.close();
            }
        }
    }

    public static void createDir(final String dir) {
        (new File(dir)).mkdirs();
    }

    public static void deleteDirContents(final String dir) {
        final File directory = new File(dir);
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            for (int i = 0; i < files.length; files[i++].delete()) {
                ;
            }
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            final byte[] buffer = new byte[BUFFER_SIZE];
            for (int numberOfBytesRead; (numberOfBytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1; outputStream.write(
                    buffer, 0, numberOfBytesRead)) {
                ;
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    public static String getTemporaryFileBaseName() {
        return TEMPORARY_FILE_GLOBAL_UNIQUE_NAME_PREFIX;
    }

    public static File createTemporaryFile() throws IOException {
        final File temporaryFile = File.createTempFile(TEMPORARY_FILE_GLOBAL_UNIQUE_NAME_PREFIX, "");
        // In case anything fails the file will be cleaned when jvm
        // shutsdown
        temporaryFile.deleteOnExit();
        return temporaryFile;
    }

    public static File copyToTemporaryFile(final InputStream inputStream) throws IOException {
        final File temporaryFile = createTemporaryFile();

        FileOutputStream targetFileOutputStream = null;
        try {
            targetFileOutputStream = new FileOutputStream(temporaryFile);
            copy(inputStream, targetFileOutputStream);
        } finally {
            if (targetFileOutputStream != null) {
                targetFileOutputStream.close();
            }
            inputStream.close();
        }

        return temporaryFile;
    }

    public static String getFilenameOnly(final String filename) {
        for (final char separatorChar : SEPARATOR_CHARS) {
            if (filename.lastIndexOf(separatorChar) != -1) {
                return filename.substring(filename.lastIndexOf(separatorChar) + 1);
            }
        }

        return filename;
    }

    public static File unzipFile(File file) throws IOException {
        File tempDir = Files.createTempDir();
        FileInputStream fileInputStream = new FileInputStream(file);

        FileUtils.copyFileToAnotherDirWithRelativePaths(file.getParentFile(), tempDir, file);

        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);

        ZipEntry zipEntry = zipInputStream.getNextEntry();
        File zipContentFile = null;
        File zipContentFileParentDir = null;
        while (zipEntry != null) {
            zipEntry.getName();
            zipContentFile = new File(tempDir, zipEntry.getName());
            zipContentFileParentDir = zipContentFile.getParentFile();
            zipContentFileParentDir.mkdirs();

            if (!zipEntry.isDirectory()) {
                zipContentFile.createNewFile();
            } else {
                zipContentFile.mkdirs();
            }

            zipContentFile.deleteOnExit();

            if (!zipEntry.isDirectory() && zipContentFile.exists() && zipContentFile.canWrite()) {
                OutputStream zipOs = new FileOutputStream(zipContentFile);
                ByteStreams.copy(zipInputStream, zipOs);
                zipOs.close();
            }

            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }

        return tempDir;
    }

    public static String makeRelativePath(String absoluteParentPath, String originalAbsoluteFilePath, String uniqueId) {
        if (originalAbsoluteFilePath != null && absoluteParentPath != null
                && originalAbsoluteFilePath.length() > absoluteParentPath.length()) {
            return originalAbsoluteFilePath.substring(absoluteParentPath.length() + 1);
        } else {
            return uniqueId;
        }
    }

    public static File copyFileToAnotherDirWithRelativePaths(File srcDir, File destDir, File originalFile)
            throws FileNotFoundException, IOException {
        String relativePath = makeRelativePath(srcDir.getAbsolutePath(), originalFile.getAbsolutePath(), "");
        File newFile = new File(destDir, relativePath);
        FileInputStream fis = new FileInputStream(originalFile);
        FileOutputStream fos = new FileOutputStream(newFile);
        ByteStreams.copy(fis, fos);
        fis.close();
        fos.close();
        return newFile;
    }

    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            }
            file.delete();
        }
        directory.delete();
    }
}