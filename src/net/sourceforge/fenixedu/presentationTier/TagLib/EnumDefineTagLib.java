/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:13:53,23/Set/2005
 * @version $Id$
 */
public class EnumDefineTagLib extends BodyTagSupport {
    private String id;

    private String enumerationString;

    private String locale = Globals.LOCALE_KEY;

    private String bundle;

    protected static MessageResources messages = MessageResources
	    .getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public String getEnumeration() {
	return enumerationString;
    }

    public void setEnumeration(String enumeration) {
	this.enumerationString = enumeration;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getLocale() {
	return locale;
    }

    public void setLocale(String locale) {
	this.locale = locale;
    }

    public int doStartTag() throws JspException {
	pageContext.getRequest().setAttribute(id, getLabel());

	return super.doStartTag();
    }

    /**
     * @return
     */
    private String getLabel() {
	String result = "variable undefined";
	Enum enumeration = (Enum) pageContext.findAttribute(this.enumerationString);
	try {
	    String message = RequestUtils.message(pageContext, this.bundle, this.locale, enumeration.toString(), null);
	    if (message != null)
		result = message;
	} catch (JspException e) {
	    throw new IllegalArgumentException("Expected an enum type, got: " + enumeration);
	}

	return result;
    }
}
