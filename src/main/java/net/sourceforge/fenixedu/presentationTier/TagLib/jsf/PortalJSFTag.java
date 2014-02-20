package net.sourceforge.fenixedu.presentationTier.TagLib.jsf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.fenixedu.bennu.portal.StrutsPortalBackend;

public class PortalJSFTag extends TagSupport {

    private static final long serialVersionUID = 7598687970107322370L;
    private String actionClass;

    public void setActionClass(String actionClass) {
        this.actionClass = actionClass;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Class<?> actionClass = Class.forName(this.actionClass);
            StrutsPortalBackend.chooseSelectedFunctionality((HttpServletRequest) pageContext.getRequest(), actionClass);
            return SKIP_BODY;
        } catch (ClassNotFoundException e) {
            throw new JspException("Cannot find action class", e);
        }
    }

}
