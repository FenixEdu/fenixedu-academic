package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.JspException;

public class AcademicGroupNotAllowedTagLib extends AcademicGroupTagLib {

    /**
     * 
     */
    private static final long serialVersionUID = 1529594302892469648L;

    @Override
    public int doStartTag() throws JspException {
	int result = super.doStartTag();

	if (result == EVAL_BODY_INCLUDE) {
	    return SKIP_BODY;
	}
	return EVAL_BODY_INCLUDE;
    }
}
