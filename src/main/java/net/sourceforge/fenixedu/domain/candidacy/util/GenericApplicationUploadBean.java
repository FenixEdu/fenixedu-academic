/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.GenericApplication;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationFile;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationLetterOfRecomentation;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationRecomentation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;

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
        InputStream stream = this.getStream();
        long fileLength = this.getFileSize();

        if (stream == null || fileLength == 0) {
            return null;
        }

        if (fileLength > MAX_FILE_SIZE) {
            throw new DomainException("error.file.to.big");
        }
        byte[] contents = new byte[(int) fileLength];
        stream.read(contents);
        PdfReader pdfFile = new PdfReader(contents);

        return contents;
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
