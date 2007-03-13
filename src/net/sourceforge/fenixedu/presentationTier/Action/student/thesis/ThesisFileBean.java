package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import java.io.InputStream;
import java.io.Serializable;

public class ThesisFileBean implements Serializable {

    /**
     * Serial Version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private String fileName;
    private Long fileSize;
    
    transient private InputStream file;

    public InputStream getFile() {
        return this.file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSimpleFileName() {
        String name = getFileName();
        
        if (name == null) {
            return null;
        }

        char separator;
        if (name.matches("[\\p{Alpha}]:\\\\.*")) {
            separator = '\\';
        }
        else {
            separator = '/';
        }
        
        return name.substring(name.lastIndexOf(separator) + 1);
    }
    
}
