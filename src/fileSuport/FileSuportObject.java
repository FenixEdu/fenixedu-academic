/*
 * Created on 31/Jul/2003
 *
 * 
 */
package fileSuport;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @author João Mota
 * 
 * 31/Jul/2003 fenix-head fileSuport
 *  
 */
public class FileSuportObject implements Serializable {

    private String uri;

    private String fileName;

    private String contentType;

    private byte[] content;

    private String linkName;

    private String rootUri;

    /**
     *  
     */
    public FileSuportObject() {
    }

    /**
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName) {
        try {
            this.fileName = new String(fileName.getBytes("ISO-8859-1"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            this.fileName = fileName;
        }
    }

    /**
     * @return
     */
    public boolean isStored() {
        return false;
    }

    /**
     * @return
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     */
    public void setUri(String uri) {
        try {
            this.uri = new String(uri.getBytes("ISO-8859-1"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            this.uri = uri;
        }

    }

    /**
     * @return
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * @param linkName
     */
    public void setLinkName(String linkName) {
        try {
            this.linkName = new String(linkName.getBytes("ISO-8859-1"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            this.linkName = linkName;
        }
    }

    /**
     * @return
     */
    public String getRootUri() {
        return rootUri;
    }

    /**
     * @param rootUri
     */
    public void setRootUri(String rootUri) {
        try {
            this.rootUri = new String(rootUri.getBytes("ISO-8859-1"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            this.rootUri = rootUri;
        }

    }

}