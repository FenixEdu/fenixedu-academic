package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class RootDomainObjectDefiner extends BodyTagSupport {

    private String scope;
    private String id;

    public String getScope() {
	return scope;
    }

    public void setScope(String scope) {
	this.scope = scope;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public int getPageScope() {
	if (scope == null) {
	    return PageContext.PAGE_SCOPE;
	} else if (scope.equalsIgnoreCase("page")) {
	    return PageContext.PAGE_SCOPE;
	} else if (scope.equalsIgnoreCase("request")) {
	    return PageContext.REQUEST_SCOPE;
	} else if (scope.equalsIgnoreCase("session")) {
	    return PageContext.SESSION_SCOPE;
	} else {
	    return PageContext.PAGE_SCOPE;
	}
    }
    
    @Override
    public int doEndTag() throws JspException {
	pageContext.setAttribute(getId(),RootDomainObject.getInstance(), getPageScope());
	return super.doEndTag();
    }

}
