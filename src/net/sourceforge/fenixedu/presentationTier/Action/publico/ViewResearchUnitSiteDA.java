package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class ViewResearchUnitSiteDA extends UnitSiteVisualizationDA {


    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return frontPage(mapping, form, request, response);
    }

    public ActionForward frontPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return presentation(mapping, form, request, response);
    }

    public ActionForward showResearchers(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ResearchUnitSite site = getSite(request);
	request.setAttribute("researchUnit", site.getUnit());
	return mapping.findForward("showResearchers");
    }

    @Override
    protected String getContextParamName(HttpServletRequest request) {
	return "siteID";
    }

    @Override
    protected Unit getUnit(HttpServletRequest request) {
	return getSite(request).getUnit();
    }

    private ResearchUnitSite getSite(HttpServletRequest request) {
	Container container = AbstractFunctionalityContext.getCurrentContext(request)
		.getSelectedContainer();
	if (container == null) {
	    String siteID = request.getParameter(getContextParamName(request));
	    return (ResearchUnitSite) rootDomainObject.readContentByOID(Integer.valueOf(siteID));
	} else {
	    return (ResearchUnitSite) container;
	}
    }

   @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
	ResearchUnitSite site = getSite(request);

	try {
	    MetaDomainObject metaDomainObject = MetaDomainObject.getMeta(ResearchUnitSite.class);
	    String path = metaDomainObject.getAssociatedPortal().getNormalizedName().getContent() + "/" + site.getUnit().getUnitPath("/");
	    return RequestUtils.absoluteURL(request,
		    path).toString();
	} catch (MalformedURLException e) {
	    return null;
	}
    }

}
