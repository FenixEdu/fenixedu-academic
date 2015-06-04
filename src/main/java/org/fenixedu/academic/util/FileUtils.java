/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * @(#)FileUtils.java Created on Nov 5, 2004
 * 
 */
package org.fenixedu.academic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
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

    // Cluster safe global unique temporary filename
    private static final String TEMPORARY_FILE_GLOBAL_UNIQUE_NAME_PREFIX = UUID.randomUUID().toString();

    private static final char[] SEPARATOR_CHARS = new char[] { '\\', '/' };

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
            ByteStreams.copy(inputStream, targetFileOutputStream);
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

        FileUtils.copyFileToAnotherDirWithRelativePaths(file.getParentFile(), tempDir, file);

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file))) {
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
                    try (OutputStream zipOs = new FileOutputStream(zipContentFile)) {
                        ByteStreams.copy(zipInputStream, zipOs);
                    }
                }

                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
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
        try (FileInputStream fis = new FileInputStream(originalFile); FileOutputStream fos = new FileOutputStream(newFile);) {
            ByteStreams.copy(fis, fos);
            return newFile;
        }
    }

}