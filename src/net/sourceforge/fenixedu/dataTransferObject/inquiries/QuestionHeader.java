/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class QuestionHeader implements Serializable {

    private String title;

    private String[] scaleHeaders;

    public QuestionHeader(String title) {
	this.title = title;
    }

    public QuestionHeader(String title, String... scaleHeaders) {
	this(title);
	this.scaleHeaders = scaleHeaders;
    }

    public String getTitle() {
	return title;
    }

    public String[] getScaleHeaders() {
	return scaleHeaders;
    }

    public boolean hasScaleHeaders() {
	return scaleHeaders != null;
    }

    public int getScaleHeadersCount() {
	return scaleHeaders != null ? scaleHeaders.length : 1;
    }

}
