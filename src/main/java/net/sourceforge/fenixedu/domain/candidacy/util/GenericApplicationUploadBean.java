package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import pt.ist.fenixWebFramework.services.Service;

import net.sourceforge.fenixedu.domain.candidacy.GenericApplication;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationFile;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationLetterOfRecomentation;
import net.sourceforge.fenixedu.domain.candidacy.GenericApplicationRecomentation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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

        return contents;
    }

    @Service
    public void uploadTo(final GenericApplication application) {
        try {
            final byte[] content = readStreamContents();
            new GenericApplicationFile(application, displayName, fileName, content);
        } catch (final IOException ex) {
            throw new Error(ex);
        }
    }

    @Service
    public void uploadTo(final GenericApplicationRecomentation recomentation) {
        try {
            final byte[] content = readStreamContents();
            if (recomentation.hasLetterOfRecomentation()) {
                recomentation.getLetterOfRecomentation().delete();
            }                
            new GenericApplicationLetterOfRecomentation(recomentation, displayName, fileName, content);
        } catch (final IOException ex) {
            throw new Error(ex);
        }
    }

}
