package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.InputStream;
import java.io.Serializable;

public class OpenFileBean implements Serializable {
    
    private transient InputStream inputStream;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
