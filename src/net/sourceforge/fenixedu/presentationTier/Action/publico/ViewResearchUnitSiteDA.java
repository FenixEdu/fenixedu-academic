package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ResearchUnitProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class ViewResearchUnitSiteDA extends SiteVisualizationDA {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResearchUnitSite site = getSite(request);
		if (site != null) {
			request.setAttribute("site", site);
		}
		return super.execute(mapping, form, request, response);
	}

	public ActionForward frontPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ResearchUnitSite site = getSite(request);
		request.setAttribute("researchUnit", site.getUnit());
		if (site.getShowEvents()) {
			request.setAttribute("eventAnnouncements", getEventAnnouncements(site));
		}
		if (site.getShowAnnouncements()) {
			request.setAttribute("announcements", getAnnouncements(site));
		}

		String redirect = (site.getLayout() != null) ? "frontPage-" + site.getLayout() : "frontPage";
		return mapping.findForward(redirect);
	}

	public ActionForward showResearchers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ResearchUnitSite site = getSite(request);
		request.setAttribute("researchUnit", site.getUnit());
		return mapping.findForward("showResearchers");
	}

	public ActionForward showPublications(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResearchUnit unit = getSite(request).getUnit();
		putPublicationsInRequest(request,unit);
		return mapping.findForward("showPublications");
	}
	
	private void putPublicationsInRequest(HttpServletRequest request, ResearchUnit unit) {
		request.setAttribute("books", ResearchResultPublication.sort(unit.getBooks()));
		request.setAttribute("national-articles", ResearchResultPublication.sort(unit
			.getArticles(ScopeType.NATIONAL)));
		request.setAttribute("international-articles", ResearchResultPublication.sort(unit
			.getArticles(ScopeType.INTERNATIONAL)));
		request.setAttribute("national-inproceedings", ResearchResultPublication.sort(unit
			.getInproceedings(ScopeType.NATIONAL)));
		request.setAttribute("international-inproceedings", ResearchResultPublication.sort(unit
			.getInproceedings(ScopeType.INTERNATIONAL)));
		request.setAttribute("proceedings", ResearchResultPublication.sort(unit.getProceedings()));
		request.setAttribute("theses", ResearchResultPublication.sort(unit.getTheses()));
		request.setAttribute("manuals", ResearchResultPublication.sort(unit.getManuals()));
		request
			.setAttribute("technicalReports", ResearchResultPublication
				.sort(unit.getTechnicalReports()));
		request.setAttribute("otherPublications", ResearchResultPublication.sort(unit
			.getOtherPublications()));
		request.setAttribute("unstructureds", ResearchResultPublication.sort(unit.getUnstructureds()));
		request.setAttribute("inbooks", ResearchResultPublication.sort(unit.getInbooks()));
		
	}

	private List<Announcement> getEventAnnouncements(ResearchUnitSite site) {
		PartyAnnouncementBoard eventBoard = getEventBoards(site.getUnit());
		List<Announcement> announcements = (eventBoard != null) ? eventBoard.getAnnouncements()
				: new ArrayList<Announcement>();
		return announcements;
	}

	private List<Announcement> getAnnouncements(ResearchUnitSite site) {
		PartyAnnouncementBoard announcementBoard = getAnnouncementBoards(site.getUnit());
		List<Announcement> announcements = (announcementBoard != null) ? announcementBoard
				.getAnnouncements() : new ArrayList<Announcement>();
		return announcements;
	}

	@Override
	protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return frontPage(mapping, form, request, response);
	}

	private PartyAnnouncementBoard getBoardByName(ResearchUnit unit, String name) {
		for (PartyAnnouncementBoard board : unit.getBoards()) {
			if (board.getName().equals(name)) {
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

		return (possibleResearchUnitSite instanceof ResearchUnitSite) ? (ResearchUnitSite) possibleResearchUnitSite
				: null;
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
