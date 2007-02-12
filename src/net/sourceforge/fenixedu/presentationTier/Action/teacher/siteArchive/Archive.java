package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * The <tt>Archive</tt> coordinates the storage of the retrieved resources.
 * 
 * <p>
 * The user creates an archive with a servlet response. Then, for each resource
 * to include in the archive, she calls {@link #getStream(Resource)} and writes
 * the resource content to it. After all resources are retrived {@link #finish()}
 * is called ensuring that all content is written to the servlet response. 
 * 
 * <p>
 * <strong>Note</strong>
 * however that each intermediary stream may already be writing to the response
 * so the user should not use the response after creating the archive.
 * 
 * @see #getStream(Resource)
 * @see #finish()
 * 
 * @author cfgi
 */
public abstract class Archive {

    private HttpServletResponse response;
    private String name;

    public Archive(HttpServletResponse response, String name) {
        super();
    
        this.response = response;
        this.name = name;
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }
    
    /**
     * Obtains a new stream that can use to store the resource. In this
     * operation the resource name is important but each archive may process the
     * name differently. As a general rule, the resource name should use a path
     * like format that does not start with <tt>'/'</tt> like <tt>"file"</tt>
     * or <tt>"dira/dirb/file"</tt>.
     * 
     * @param resource
     *            the target resource
     * @return a stream where the resource's contents should be written
     *         
     * @throws IOException when an io error occurs 
     */
    public abstract OutputStream getStream(Resource resource) throws IOException;
    
    /**
     * Finishes the archive ensuring that all content is written to the response and
     * terminating any pending resources.
     * 
     * @throws IOException
     */
    public abstract void finish() throws IOException;
}
