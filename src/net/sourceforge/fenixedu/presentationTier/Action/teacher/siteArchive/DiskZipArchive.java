package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import pt.utl.ist.fenix.tools.file.utils.FileUtils;

/**
 * The <tt>DiskZipArchive</tt> saves all contents to disk and when
 * {@link #finish()} is called creates the Zip file and writes it to the
 * response.
 * 
 * @author cfgi
 */
public class DiskZipArchive extends DiskArchive {

    public DiskZipArchive(HttpServletResponse response, String name) throws IOException {
        super(getTemporaryDirectory(), response);
        
        setName(name);
    }

    /**
     * Creates a Zip file from all contents saved in the disk and writes it into
     * the response.
     */
    @Override
    public void finish() throws IOException {
        File root = getRoot();
        
        HttpServletResponse response = getResponse();
        
        response.setContentType(ZipArchive.ARCHIVE_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + getName() + ".zip\"");
        
        ZipOutputStream zipStream = new ZipOutputStream(response.getOutputStream());
        
        writeZipEntries(zipStream, "", root);
        
        zipStream.close();
        
        FileUtils.deleteDirectory(getRoot());
    }

    private void writeZipEntries(ZipOutputStream zipStream, String prefix, File dir) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                writeZipEntries(zipStream, prefix + file.getName() + "/", file);
            }
            else {
                writeZipEntry(zipStream, prefix, file);
            }
        }
    }

    private void writeZipEntry(ZipOutputStream zipStream, String prefix, File file) throws IOException {
        ZipEntry entry = new ZipEntry(prefix + file.getName());
        zipStream.putNextEntry(entry);

        FileInputStream fileStream = new FileInputStream(file);

        byte[] buffer = new byte[2048];
        int length;
        
        while ((length = fileStream.read(buffer)) != -1) {
            zipStream.write(buffer, 0, length);
        }
        
        fileStream.close();
        zipStream.closeEntry();
    }
    
}
