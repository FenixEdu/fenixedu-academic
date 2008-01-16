package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;

public class ContentProcessor extends SiteElementPathProcessor {

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
	return new ContentContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
	ContentContext ownContext = (ContentContext) context;

	int i;
	for (i = 0;; i++) {
	    String current = provider.peek(i);

	    if (!ownContext.addContent(current)) {
		break;
	    }
	}

	while (i > 1) {
	    provider.next();
	    i--;
	}

	return !ownContext.getContents().isEmpty();
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
	if (provider.hasNext()) {
	    return false;
	} else {
	    ContentContext ownContext = (ContentContext) context;
	    if (ownContext.getContents().isEmpty()) {
		return false;
	    }

	    HttpServletRequest request = ownContext.getRequest();
	    FilterFunctionalityContext filterContext = (FilterFunctionalityContext) request
		    .getAttribute(FunctionalityContext.CONTEXT_KEY);
	    if (filterContext == null) {
		filterContext = new FilterFunctionalityContext(request, ownContext.getContents());
		filterContext.setHasBeenForwarded();
		request.setAttribute(FunctionalityContext.CONTEXT_KEY, filterContext);
	    }
	    filterContext.addAllContent(ownContext.getContents());

	    String url = filterContext.getCurrentContextPath();

	    context.getResponse().sendRedirect(request.getContextPath() + url);
	    return true;
	}
    }

    public static class ContentContext extends ProcessingContext {

	private List<Content> contents;

	public ContentContext(ProcessingContext parent) {
	    super(parent);

	    this.contents = new ArrayList<Content>();
	}

	public boolean addContent(String name) {
	    for (Content content : getCurrentPossibilities()) {
		String pathName = getElementPathName(content);

		if (pathName == null) {
		    continue;
		}

		if (pathName.equalsIgnoreCase(name)) {
		    addContents(content);
		    return true;
		}
	    }

	    return false;
	}

	public Site getSite() {
	    return ((SiteContext) getParent()).getSite();
	}

	public String getContextURI() {
	    return ((SiteContext) getParent()).getSiteBasePath() + "&sectionID=%s";
	}

	private Collection<Content> getCurrentPossibilities() {
	    Content content = getLastContent();

	    if (content == null) {
		Site site = getSite();
		if (site == null) {
		    return Collections.emptyList();
		}
		List<Content> contents = new ArrayList<Content>();
		for (Content childContent : site.getChildrenAsContent()) {
		    contents.add(childContent);
		}
		return contents;
	    } else {
		return (content.isContainer()) ? ((Container) content).getChildrenAsContent() : Collections.EMPTY_LIST;
	    }
	}

	public Content getLastContent() {
	    List<Content> contents = getContents();

	    if (contents.isEmpty()) {
		return null;
	    } else {
		return contents.get(contents.size() - 1);
	    }
	}

	public void addContents(Content content) {
	    this.contents.add(content);
	}

	public List<Content> getContents() {
	    return this.contents;
	}

    }

    public static String getSectionPath(Section section) {
	if (section == null) {
	    return "";
	} else {
	    return getSectionPath(section.getSuperiorSection()) + "/" + getElementPathName(section);
	}
    }

}
