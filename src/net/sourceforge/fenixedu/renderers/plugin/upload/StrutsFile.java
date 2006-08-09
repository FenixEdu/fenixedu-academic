package net.sourceforge.fenixedu.renderers.plugin.upload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.struts.upload.FormFile;

/**
 * This is a wrapper around a {@link org.apache.struts.upload.FormFile form file} from 
 * Struts upload.
 * 
 * @author cfgi
 */
public class StrutsFile implements UploadedFile {

    private FormFile strutsFile;
    
    public StrutsFile(FormFile strutsFile) {
        super();
        
        this.strutsFile = strutsFile;
    }

    public String getName() {
        return this.strutsFile.getFileName();
    }

    public String getContentType() {
        return this.strutsFile.getContentType();
    }

    public long getSize() {
        return this.strutsFile.getFileSize();
    }

    public InputStream getInputStream() throws IOException {
        return this.strutsFile.getInputStream();
    }

}
