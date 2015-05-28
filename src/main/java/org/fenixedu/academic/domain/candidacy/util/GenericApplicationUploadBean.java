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
package org.fenixedu.academic.domain.candidacy.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.fenixedu.academic.domain.candidacy.GenericApplication;
import org.fenixedu.academic.domain.candidacy.GenericApplicationFile;
import org.fenixedu.academic.domain.candidacy.GenericApplicationLetterOfRecomentation;
import org.fenixedu.academic.domain.candidacy.GenericApplicationRecomentation;
import org.fenixedu.academic.domain.exceptions.DomainException;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;
import com.lowagie.text.pdf.PdfReader;

public class GenericApplicationUploadBean implements Serializable {

    private static final long serialVersionUID = 1L;

    protected transient InputStream stream;
    protected long fileSize;
    protected String fileName;
    protected String displayName;

    public GenericApplicationUploadBean() {
    }

    public InputStream getStream() throws FileNotFoundException {
        return this.stream;
    }

    public void setStream(InputStream stream) throws IOException {
        this.stream = stream;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    protected static final int MAX_FILE_SIZE = 3698688;

    protected byte[] readStreamContents() throws IOException {
        try (final InputStream stream = this.getStream()) {
            if (stream == null || getFileSize() == 0) {
                return null;
            }
            if (getFileSize() > MAX_FILE_SIZE) {
                throw new DomainException("error.file.to.big");
            }
            byte[] contents = ByteStreams.toByteArray(stream);
            // Ensure the submitted file is a PDF
            new PdfReader(contents);
            return contents;
        }
    }

    @Atomic
    public GenericApplicationFile uploadTo(final GenericApplication application) {
        try {
            final byte[] content = readStreamContents();
            if (content != null && content.length > 0) {
                return new GenericApplicationFile(application, displayName, fileName, content);
            }
        } catch (final IOException ex) {
            throw new Error(ex);
        }

        return null;
    }

    @Atomic
    public GenericApplicationLetterOfRecomentation uploadTo(final GenericApplicationRecomentation recomentation) {
        try {
            final byte[] content = readStreamContents();
            if (content != null && content.length > 0) {
                if (recomentation.getLetterOfRecomentation() != null) {
                    recomentation.getLetterOfRecomentation().delete();
                }
                return new GenericApplicationLetterOfRecomentation(recomentation, displayName, fileName, content);
            }
        } catch (final IOException ex) {
            throw new Error(ex);
        }
        return null;
    }
}
