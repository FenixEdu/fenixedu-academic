package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewResearchUnitSiteDA extends SiteVisualizationDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {
        String siteID = request.getParameter("siteID");
	DomainObject possibleResearchUnitSite = rootDomainObject.readDomainObjectByOID(ResearchUnitSite.class, Integer.valueOf(siteID));
        if(possibleResearchUnitSite instanceof ResearchUnitSite) {
            request.setAttribute("site",possibleResearchUnitSite);
        }
        return super.execute(mapping, form, request, response);
    }
    
    public ActionForward frontPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	String siteID = request.getParameter("siteID");
	ResearchUnitSite site = (ResearchUnitSite) rootDomainObject.readDomainObjectByOID(ResearchUnitSite.class, Integer.valueOf(siteID));
	request.setAttribute("researchUnit", site.getUnit());
	
	return mapping.findForward("frontPage");
    }
    
    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return frontPage(mapping, form, request, response);
    }

}
