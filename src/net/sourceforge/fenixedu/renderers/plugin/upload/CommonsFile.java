package net.sourceforge.fenixedu.renderers.plugin.upload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

/**
 * This is a wrapper around a {@link org.apache.commons.fileupload.FileItem file item} 
 * from Commons Upload.
 * 
 * @author cfgi
 */
public class CommonsFile implements UploadedFile {

    private FileItem commonsFile;
    
    public CommonsFile(FileItem commonsFile) {
        super();
        
        this.commonsFile = commonsFile;
    }

    public String getName() {
        return this.commonsFile.getName();
    }

    public String getContentType() {
        return this.commonsFile.getContentType();
    }

    public long getSize() {
        return this.commonsFile.getSize();
    }

    public InputStream getInputStream() throws IOException {
        return this.commonsFile.getInputStream();
    }

}
