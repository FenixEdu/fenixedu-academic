package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

public class DegreeProcessor extends PathProcessor {

    private String degreeSiteURI;

    public DegreeProcessor(String forwardURI, String degreeSiteURI) {
	super(forwardURI);

	this.degreeSiteURI = degreeSiteURI;
    }

    public DegreeProcessor add(ContentProcessor processor) {
	addChild(processor);
	return this;
    }

    public DegreeProcessor add(ExecutionCoursesProcessor processor) {
	addChild(processor);
	return this;
    }

    public DegreeProcessor add(ScheduleProcessor processor) {
	addChild(processor);
	return this;
    }

    public DegreeProcessor add(ExamProcessor processor) {
	addChild(processor);
	return this;
    }

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parent) {
	return new DegreeContext(parent, this.degreeSiteURI);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
	String current = provider.current();
	Degree degree = Degree.readBySigla(current);

	if (degree == null) {
	    return false;
	} else {
	    DegreeContext ownContext = (DegreeContext) context;
	    ownContext.setDegree(degree);

	    /*
	     * This is an ugly hack so this process still works until the
	     * semester ends where we can easily notify everyone that the link
	     * format has changed.
	     * 
	     * 04-02-2007 pcma
	     */
	    DegreeSite portalInstance = degree.getSite();
	    MetaDomainObjectPortal portal = (MetaDomainObjectPortal) MetaDomainObject.getMeta(portalInstance.getClass())
		    .getAssociatedPortal();

	    List<Content> contents = new ArrayList<Content>();
	    contents.add(portal);
	    contents.add(portalInstance);

	    HttpServletRequest request = context.getRequest();
	    FilterFunctionalityContext filterContext = new FilterFunctionalityContext(request, contents);
	    filterContext.setHasBeenForwarded();
	    request.setAttribute(FunctionalityContext.CONTEXT_KEY, filterContext);
	    return true;
	}
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
	if (!provider.hasNext()) {
	    DegreeContext ownContext = (DegreeContext) context;
	    String url = ownContext.getDegree().getSite().getReversePath();
	    context.getResponse().sendRedirect(ownContext.getRequest().getContextPath() + url);
	    return true;
	} else {
	    return false;
	}
    }

    public static class DegreeContext extends ProcessingContext implements SiteContext {

	private String contextURI;

	public DegreeContext(ProcessingContext parent, String contextURI) {
	    super(parent);

	    this.contextURI = contextURI;
	}

	private Degree degree;

	public Degree getDegree() {
	    return this.degree;
	}

	public void setDegree(Degree degree) {
	    this.degree = degree;
	}

	public Site getSite() {
	    return getDegree().getSite();
	}

	public String getSiteBasePath() {
	    return String.format(this.contextURI, "%s", getDegree().getIdInternal());
	}

    }

    public static String getDegreePath(Degree degree) {
	return "/" + degree.getSigla().toLowerCase();
    }
}
