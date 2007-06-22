package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageResearchUnitAnnoucementsDA extends UnitSiteBoardsDA {

	protected ResearchUnitSite getSite(HttpServletRequest request) {
		String siteID = request.getParameter("siteID");
		if (siteID != null) {
			ResearchUnitSite site = (ResearchUnitSite) RootDomainObject
					.readDomainObjectByOID(ResearchUnitSite.class, Integer
							.valueOf(siteID));
			return site;
		} else {
			return null;
		}
	}

	@Override
	public Unit getUnit(HttpServletRequest request) {
		return getSite(request).getUnit();
	}
	
	public ActionForward editAnnouncementBoards(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResearchUnitSite site = getSite(request);
		List<PartyAnnouncementBoard> boards = site.getUnit().getBoards();
		request.setAttribute("announcementBoards", boards);
		return mapping.findForward(boards.isEmpty() ? "noBoards"
				: "listAnnouncementBoards");
	}

	@Override
	public String getContextParamName() {
		return "siteID";
	}
	
	@Override
	protected String getBoardName(HttpServletRequest request) {
		return request.getMethod().equals("viewAnnouncements") ? UnitSiteBoardsDA.ANNOUNCEMENTS : UnitSiteBoardsDA.EVENTS;
	}

	@Override
	protected String getActionPath(HttpServletRequest request) {
		return "/researchSite/manageResearchUnitAnnouncements.do";
	}
	
	@Override
	public ActionForward viewAnnouncements(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		setReturnMethodToView(request);
		return super.viewAnnouncements(mapping, form, request, response);
	}
	
	public ActionForward viewEvents(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		this.viewAnnouncements(mapping, form, request, response);
		return mapping.findForward("listEvents");
	}
	
	public ActionForward viewEvent(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		super.viewAnnouncement(mapping, form, request, response);
		return mapping.findForward("viewEvent");
	} 

	private void setReturnMethodToView(HttpServletRequest request) { 
		request.setAttribute("returnMethod", "viewAnnouncements");
	}

	@Override
	public ActionForward viewArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.viewArchive(mapping, form, request, response);
		/*
		 * Major refactor needed :-(
		 */
		return mapping.findForward(getBoardName(request).equals(UnitSiteBoardsDA.ANNOUNCEMENTS) ? "listAnnouncements" : "listEvents"); 
	}
}
