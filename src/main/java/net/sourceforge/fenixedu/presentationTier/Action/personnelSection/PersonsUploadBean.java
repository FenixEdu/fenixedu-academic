package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.io.InputStream;
import java.io.Serializable;

public class PersonsUploadBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient InputStream inputStream;
    private String filename;

    public PersonsUploadBean() {
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

}