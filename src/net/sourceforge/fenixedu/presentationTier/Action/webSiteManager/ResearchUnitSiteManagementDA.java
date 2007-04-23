package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;

public class ResearchUnitSiteManagementDA extends SiteManagementDA {

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {	
		return mapping.findForward("editResearchSite");
	}

    @Override
	protected ResearchUnitSite getSite(HttpServletRequest request) {
		String siteID = request.getParameter("oid");
		if(siteID!=null) {
			ResearchUnitSite site = (ResearchUnitSite) RootDomainObject.readDomainObjectByOID(ResearchUnitSite.class, Integer.valueOf(siteID));
			return site;
		}
		else {
			return null;
		}
	}
	
	@Override
	protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
		return getSite(request).getUnit().getName();
	}

	@Override
	protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
		return null;
	}

}
