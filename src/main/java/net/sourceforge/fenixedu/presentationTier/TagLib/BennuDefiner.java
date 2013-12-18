package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.fenixedu.bennu.core.domain.Bennu;

public class BennuDefiner extends BodyTagSupport {

    private String scope;
    private String id;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
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
        pageContext.setAttribute(getId(), Bennu.getInstance(), getPageScope());
        return super.doEndTag();
    }

}
