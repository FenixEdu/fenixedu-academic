package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.Action.CreateContentsContextAction;

import org.apache.struts.action.ActionForward;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/alumniRedirect", scope = "session")
public class AlumniLoginRedirectAction extends CreateContentsContextAction {

    @Override
    protected ActionForward menuActionForward(Content content, HttpServletRequest request) {
	ActionForward actionForward = new ActionForward();
	actionForward.setRedirect(true);

	actionForward.setPath("/alumni-?"
		+ pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
		+ pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.calculateChecksum(request
			.getContextPath() + "/alumni-"));
	return actionForward;
    }

}
