package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.Action.CreateContentsContextAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;

import org.apache.struts.action.ActionForward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/alumniRedirect", scope = "session")
public class AlumniLoginRedirectAction extends CreateContentsContextAction {

    @Override
    protected ActionForward menuActionForward(Content content, HttpServletRequest request) {
	ActionForward actionForward = new ActionForward();
	actionForward.setRedirect(true);

	actionForward.setPath("/alumni-?" + ChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
		+ ChecksumRewriter.calculateChecksum(request.getContextPath() + "/alumni-"));
	return actionForward;
    }

}
