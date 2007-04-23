package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewResearchUnitSiteDA extends SiteVisualizationDA {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResearchUnitSite site = getSite(request);
		if (site!=null) {
			request.setAttribute("site", site);
		}
		return super.execute(mapping, form, request, response);
	}

	public ActionForward frontPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ResearchUnitSite site = getSite(request);
		request.setAttribute("researchUnit", site.getUnit());

		return mapping.findForward("frontPage");
	}

	public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
	
		ResearchUnitSite site = getSite(request);
		request.setAttribute("announcements", getEventBoards(site.getUnit()));
		return mapping.findForward("showBoardAnnouncements");
	}

	public ActionForward showAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ResearchUnitSite site = getSite(request);
		request.setAttribute("announcements", getAnnouncementBoards(site.getUnit()));
		return mapping.findForward("showBoardAnnouncements");
	}
	
	@Override
	protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return frontPage(mapping, form, request, response);
	}
	
	private PartyAnnouncementBoard getBoardByName(ResearchUnit unit, String name) {
		for(PartyAnnouncementBoard board : unit.getBoards()) {
			if(board.getName().equals(name)) {
				return board;
			}
		}
		return null;
	}
	
	protected PartyAnnouncementBoard getEventBoards(ResearchUnit unit) {
		// autch :-(
		return getBoardByName(unit, "Eventos");
	}
	
	protected PartyAnnouncementBoard getAnnouncementBoards(ResearchUnit unit) {
		return getBoardByName(unit, "Anúncios");
	}

	private ResearchUnitSite getSite(HttpServletRequest request) {
		String siteID = request.getParameter("siteID");
		DomainObject possibleResearchUnitSite = rootDomainObject.readDomainObjectByOID(
				ResearchUnitSite.class, Integer.valueOf(siteID));
		
		return (possibleResearchUnitSite instanceof ResearchUnitSite) ? (ResearchUnitSite) possibleResearchUnitSite : null;
	}
}
 