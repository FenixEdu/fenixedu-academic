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
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;
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
