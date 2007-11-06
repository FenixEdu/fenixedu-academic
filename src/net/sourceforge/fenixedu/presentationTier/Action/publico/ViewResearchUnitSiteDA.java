package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ResearchUnitProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class ViewResearchUnitSiteDA extends UnitSiteVisualizationDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ResearchUnitSite site = getSite(request);
	request.setAttribute("researchUnit", site.getUnit());

	
	
	return super.execute(mapping, form, request, response);
    }

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
	String siteID = request.getParameter(getContextParamName(request));
	return (ResearchUnitSite) rootDomainObject.readSiteByOID(Integer.valueOf(siteID));
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
	ResearchUnitSite site = getSite(request);

	try {
	    return RequestUtils.absoluteURL(request,
		    ResearchUnitProcessor.getResearchUnitPath(site.getUnit())).toString();
	} catch (MalformedURLException e) {
	    return null;
	}
    }

}
