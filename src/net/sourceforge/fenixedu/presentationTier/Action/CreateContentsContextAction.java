package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MenuEntry;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateContentsContextAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final FunctionalityContext functionalityContext = new FilterFunctionalityContext(request);
	request.setAttribute(FunctionalityContext.CONTEXT_KEY, functionalityContext);

	final MenuEntry initialMenuEntry = getInitialMenuEntry(functionalityContext);
	Content content = initialMenuEntry.getReferingContent();
	if (content.isContainer()) {
	    Container container = (Container) content;
	    if (container.getInitialContent() != null) {
		content = container.getInitialContent();
	    }
	}

	return menuActionForward(content, request);
    }

    private ActionForward menuActionForward(Content content, HttpServletRequest request) {
	ActionForward actionForward = new ActionForward();
	actionForward.setRedirect(true);
	final String path = content.getPath();
	final String seperator = path.indexOf('?') >= 0 ? "&" : "?";
	actionForward.setPath(path + seperator + ChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
		+ ChecksumRewriter.calculateChecksum(request.getContextPath() + path));
	return actionForward;
    }

    private MenuEntry getInitialMenuEntry(FunctionalityContext functionalityContext) {
	for (MenuEntry menuEntry : Portal.getRootPortal().getMenu()) {
	    if (menuEntry.isVisible() && !(menuEntry.getReferingContent() instanceof MetaDomainObjectPortal)
		    && menuEntry.getReferingContent().isAvailable(functionalityContext)) {
		return menuEntry;
	    }
	}
	return null;
    }

}
