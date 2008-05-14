package net.sourceforge.fenixedu.presentationTier.formbeans;

import org.apache.struts.action.ActionForm;

public class FenixActionForm extends ActionForm {
    protected static final long serialVersionUID = 1L;

    protected String method;

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }
}
