package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageResearchUnitAnnoucementsDA extends AnnouncementManagement {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setUnit(request);
		return super.execute(mapping, actionForm, request, response);
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

	public ActionForward viewEvent(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		super.viewAnnouncement(mapping, form, request, response);
		return mapping.findForward("viewEvent");
	} 

	private void setUnit(HttpServletRequest request) {
		request.setAttribute("site", getSite(request));

	}

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
	protected Collection<AnnouncementBoard> boardsToView(
			HttpServletRequest request) throws Exception {
		ResearchUnitSite site = getSite(request);
		List<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
		ResearchUnit unit = site.getUnit();

		if (unit == null) {
			return boards;
		}

		IUserView userView = getUserView(request);
		for (AnnouncementBoard board : unit.getBoards()) {
			if (board.getReaders() == null) {
				boards.add(board);
			}
			else if (board.getReaders().allows(userView)) {
				boards.add(board);
			}
		}

		return boards;
	}

	@Override
	protected String getContextInformation(HttpServletRequest request) {
		return "/researchSite/manageResearchUnitAnnouncements.do";
	}

	@Override
	protected String getExtraRequestParameters(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();

		addExtraParameter(request, builder, "siteID");

		return builder.toString();
	}

	private void addExtraParameter(HttpServletRequest request,
			StringBuilder builder, String name) {
		String parameter = request.getParameter(name);
		if (parameter != null) {
			if (builder.length() != 0) {
				builder.append("&amp;");
			}

			builder.append(name + "=" + parameter);
		}
	}

	@Override
	public ActionForward viewAnnouncements(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		setReturnMethodToView(request);
		return super.viewAnnouncements(mapping, form, request, response);
	}
	
	
	
	private void setReturnMethodToView(HttpServletRequest request) { 
		request.setAttribute("returnMethod", "viewAnnouncements");
	}

}