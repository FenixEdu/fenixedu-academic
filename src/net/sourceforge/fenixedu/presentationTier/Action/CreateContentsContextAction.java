package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MenuEntry;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.util.HostRedirector;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateContentsContextAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final FunctionalityContext functionalityContext = new FilterFunctionalityContext(request,
		(List<Content>) Collections.EMPTY_LIST);
	request.setAttribute(FunctionalityContext.CONTEXT_KEY, functionalityContext);

	final MenuEntry initialMenuEntry = getInitialMenuEntry(functionalityContext);
	if (initialMenuEntry == null) {
	    sendLoginRedirect(request, response);
	    return null;
	}

	Content content = initialMenuEntry.getReferingContent();
	if (content.isContainer()) {
	    Container container = (Container) content;
	    if (container.getInitialContent() != null) {
		content = container.getInitialContent();
	    }
	}

	return menuActionForward(content, request);
    }

    private void sendLoginRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
	response.sendRedirect(HostRedirector.getRedirectPageLogin(request.getRequestURL().toString()));
    }

    private ActionForward menuActionForward(Content content, HttpServletRequest request) {
	ActionForward actionForward = new ActionForward();
	actionForward.setRedirect(true);
	Portal rootPortal = RootDomainObject.getInstance().getRootPortal();
	List<Content> contents = rootPortal.getPathTo(content);

	List<String> paths = new ArrayList<String>();
	for (Content contentForPath : contents.subList(1, contents.size())) {
	    paths.add(contentForPath.getNormalizedName().getContent());
	}

	StringBuffer buffer = new StringBuffer("");
	for (String pathPart : paths) {
	    buffer.append("/");
	    buffer.append(pathPart);
	}

	String realPath = buffer.toString();
	final String seperator = realPath.indexOf('?') >= 0 ? "&" : "?";
	actionForward.setPath(realPath + seperator + ChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
		+ ChecksumRewriter.calculateChecksum(request.getContextPath() + realPath));
	return actionForward;
    }

    private MenuEntry getInitialMenuEntry(FunctionalityContext functionalityContext) {
	for (MenuEntry menuEntry : Portal.getRootPortal().getMenu()) {
	    if (menuEntry.isNodeVisible() && !(menuEntry.getReferingContent() instanceof MetaDomainObjectPortal)
		    && menuEntry.getReferingContent().isAvailable(functionalityContext)) {
		return menuEntry;
	    }
	}
	return null;
    }

}
