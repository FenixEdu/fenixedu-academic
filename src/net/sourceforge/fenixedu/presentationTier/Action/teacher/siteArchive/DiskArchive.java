package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * A <tt>DiskArchive</tt> writes all resources to disk under the root directoy given in it's creation.
 * Nothing is written to the response so it's usefull afterwards.
 * 
 * @author cfgi
 */
public class DiskArchive extends Archive {
    
    private File directory;
    
    /**
     * @param root the root directory under wich all resources will be saved
     */
    public DiskArchive(File root, HttpServletResponse response) throws IOException {
        super(response, null);
        
        this.directory = root;
    }
    
    public static File getTemporaryDirectory() throws IOException {
        File file = File.createTempFile("archive", null);
        file.delete();
        file.mkdir();
        
        return file;
    }

    protected File getRoot() {
        return this.directory;
    }
    
    /**
     * Obtains a new file stream. The stream is created based on the resource
     * name and the root directoy of the archive.
     */
    public OutputStream getStream(Resource resource) throws IOException {
        File file = new File(this.directory, resource.getName());
        
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        
        return new FileOutputStream(file);
    }

    @Override
    public String toString() {
        return String.format("Archive[%s]", this.directory);
    }

    /**
     * Does nothing.
     */
    @Override
    public void finish() throws IOException {
        // do nothing
    }
}
