package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.io.InputStream;
import java.io.Serializable;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class UsernameFileBean implements Serializable {

    private transient InputStream inputStream;

    private String filename;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = StringNormalizer.normalize(filename);
    }

}
