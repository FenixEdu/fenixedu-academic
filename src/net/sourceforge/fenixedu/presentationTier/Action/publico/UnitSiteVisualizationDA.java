package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearIntervalBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class UnitSiteVisualizationDA extends SiteVisualizationDA {

    public static final int ANNOUNCEMENTS_NUMBER = 3;

    public static final MultiLanguageString ANNOUNCEMENTS_NAME = MultiLanguageString.i18n().add("pt", "Anúncios").finish();

    public static final MultiLanguageString EVENTS_NAME = MultiLanguageString.i18n().add("pt", "Eventos").finish();

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return presentation(mapping, form, request, response);
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
	Unit unit = getUnit(request);
	Site site = unit.getSite();
	try {
	    return site == null ? null : RequestUtils.absoluteURL(request, site.getReversePath()).toString();
	} catch (MalformedURLException e) {
	    return null;
	}
    }

    protected String getContextParamName(HttpServletRequest request) {
	return "unitID";
    }

    protected Object getContextParamValue(HttpServletRequest request) {
	return getUnit(request).getIdInternal();
    }

    protected String getMappingPath(ActionMapping mapping, String name) {
	ActionForward forward = mapping.findForward(name);

	return forward == null ? null : forward.getPath();
    }

    public ActionForward presentation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Unit unit = getUnit(request);
	UnitSite site = unit.getSite();

	AnnouncementBoard announcementsBoard = null;
	AnnouncementBoard eventsBoard = null;

	for (AnnouncementBoard unitBoard : unit.getBoards()) {
	    if (unitBoard.isPublicToRead() && unitBoard.getName().equalInAnyLanguage(ANNOUNCEMENTS_NAME)) {
		announcementsBoard = unitBoard;
	    }

	    if (unitBoard.isPublicToRead() && unitBoard.getName().equalInAnyLanguage(EVENTS_NAME)) {
		eventsBoard = unitBoard;
	    }
	}

	if (announcementsBoard != null) {
	    List<Announcement> announcements = announcementsBoard.getActiveAnnouncements();
	    announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
	    request.setAttribute("announcements", announcements);
	}

	if (eventsBoard != null) {
	    request.setAttribute("announcementBoard",eventsBoard);
	    
	    YearMonthDay currentDay = new YearMonthDay();
	    List<Announcement> currentDayAnnouncements = eventsBoard.getActiveAnnouncementsFor(currentDay);
	    List<Announcement> futureAnnouncements = eventsBoard.getActiveAnnouncementsAfter(currentDay);

	    Collections.sort(futureAnnouncements, Announcement.SUBJECT_BEGIN_DATE);
	    request.setAttribute("today-events", currentDayAnnouncements);
	    request.setAttribute("future-events", futureAnnouncements);

	    int eventCount = currentDayAnnouncements.size() + futureAnnouncements.size(); 
	    if (eventCount < ANNOUNCEMENTS_NUMBER) {
		List<Announcement> announcements = eventsBoard.getActiveAnnouncementsBefore(currentDay);
		announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER - eventCount));
		request.setAttribute("eventAnnouncements", announcements);
	    }
	}

	return mapping.findForward("frontPage-" + site.getLayout());
    }

    protected UnitSite getUnitSite(HttpServletRequest request) {
	FilterFunctionalityContext context = (FilterFunctionalityContext) AbstractFunctionalityContext.getCurrentContext(request);
	Container container = (Container) context.getLastContentInPath(UnitSite.class);
	return (UnitSite) container;
    }

    protected Unit getUnit(HttpServletRequest request) {
	Unit unit = (Unit) request.getAttribute("unit");

	if (unit == null) {
	    FilterFunctionalityContext context = (FilterFunctionalityContext) AbstractFunctionalityContext
		    .getCurrentContext(request);
	    UnitSite site = (UnitSite) context.getSelectedContainer();
	    unit = site.getUnit();
	}

	return unit;
    }

    public ActionForward organization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("unit-organization");
    }

    public ActionForward subunits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Unit unit = getUnit(request);

	SortedSet<Unit> subunits = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
	for (Unit sub : unit.getSubUnits()) {
	    if (sub.hasSite()) {
		subunits.add(sub);

		String siteUrl = getSiteUrl(mapping, sub);
		request.setAttribute("viewSite" + sub.getIdInternal(), siteUrl);
	    }
	}

	request.setAttribute("subunits", subunits);
	return mapping.findForward("unit-subunits");
    }

    protected String getSiteUrl(ActionMapping mapping, Unit sub) {
	ActionForward forward = mapping.findForward("view" + sub.getClass().getSimpleName() + "Site");

	if (forward == null) {
	    forward = mapping.findForward("viewUnitSite");
	}

	return String.format(forward.getPath(), sub.getIdInternal(), sub.getSite().getIdInternal());
    }

    public ActionForward showPublications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Unit unit = getUnit(request);

	IViewState viewState = RenderUtils.getViewState("executionYearIntervalBean");

	ExecutionYearIntervalBean bean;
	if (viewState != null) {
	    bean = (ExecutionYearIntervalBean) viewState.getMetaObject().getObject();
	} else {
	    bean = generateSearchBean();
	}

	request.setAttribute("executionYearIntervalBean", bean);

	preparePublicationsForResponse(request, unit, bean);

	return mapping.findForward("showPublications");
    }

    protected void preparePublicationsForResponse(HttpServletRequest request, Unit unit, ExecutionYearIntervalBean bean) {
	putPublicationsOnRequest(request, unit, bean, Boolean.FALSE);
    }

    protected void putPublicationsOnRequest(HttpServletRequest request, Unit unit, ExecutionYearIntervalBean bean,
	    Boolean checkSubunits) {

	ExecutionYear firstExecutionYear = bean.getFirstExecutionYear();
	ExecutionYear finalExecutionYear = bean.getFinalExecutionYear();
	ResultPublicationType resultPublicationType = bean.getPublicationType();

	// String[] publicationTypes = new String[] {"articles", "books",
	// "inbooks", "inproceedings", "proceedings", "theses", "manuals",
	// "technical-reports", "other-publications", "unstructureds"};

	if (resultPublicationType == null) {
	    request.setAttribute("international-articles", ResearchResultPublication.sort(unit.getArticles(
		    ScopeType.INTERNATIONAL, firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("national-articles", ResearchResultPublication.sort(unit.getArticles(ScopeType.NATIONAL,
		    firstExecutionYear, finalExecutionYear)));
	    List<ResearchResultPublication> articles = ResearchResultPublication.sort(unit.getArticles(firstExecutionYear,
		    finalExecutionYear, checkSubunits));
	    request.setAttribute("hasArticles", !articles.isEmpty());
	    request.setAttribute("articles", articles);

	    request.setAttribute("books", ResearchResultPublication.sort(unit.getBooks(firstExecutionYear, finalExecutionYear,
		    checkSubunits)));
	    request.setAttribute("inbooks", ResearchResultPublication.sort(unit.getInbooks(firstExecutionYear,
		    finalExecutionYear, checkSubunits)));
	    request.setAttribute("international-inproceedings", ResearchResultPublication.sort(unit.getInproceedings(
		    ScopeType.INTERNATIONAL, firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("national-inproceedings", ResearchResultPublication.sort(unit.getInproceedings(
		    ScopeType.NATIONAL, firstExecutionYear, finalExecutionYear)));
	    request.setAttribute("inproceedings", ResearchResultPublication.sort(unit.getInproceedings(firstExecutionYear,
		    finalExecutionYear, checkSubunits)));
	    request.setAttribute("proceedings", ResearchResultPublication.sort(unit.getProceedings(firstExecutionYear,
		    finalExecutionYear, checkSubunits)));
	    request.setAttribute("theses", ResearchResultPublication.sort(unit.getTheses(firstExecutionYear, finalExecutionYear,
		    checkSubunits)));
	    request.setAttribute("manuals", ResearchResultPublication.sort(unit.getManuals(firstExecutionYear,
		    finalExecutionYear, checkSubunits)));
	    request.setAttribute("technical-reports", ResearchResultPublication.sort(unit.getTechnicalReports(firstExecutionYear,
		    finalExecutionYear, checkSubunits)));
	    request.setAttribute("other-publications", ResearchResultPublication.sort(unit.getOtherPublications(
		    firstExecutionYear, finalExecutionYear, checkSubunits)));
	    request.setAttribute("unstructureds", ResearchResultPublication.sort(unit.getUnstructureds(firstExecutionYear,
		    finalExecutionYear, checkSubunits)));
	} else {
	    switch (resultPublicationType) {
	    case Article:
		request.setAttribute("articles", ResearchResultPublication.sort(unit.getArticles(firstExecutionYear,
			finalExecutionYear, checkSubunits)));
		break;
	    case Book:
		request.setAttribute("books", ResearchResultPublication.sort(unit.getBooks(firstExecutionYear,
			finalExecutionYear, checkSubunits)));
		break;
	    case BookPart:
		request.setAttribute("inbooks", ResearchResultPublication.sort(unit.getInbooks(firstExecutionYear,
			finalExecutionYear, checkSubunits)));
		break;
	    case Inproceedings:
		request.setAttribute("inproceedings", ResearchResultPublication.sort(unit.getInproceedings(firstExecutionYear,
			finalExecutionYear, checkSubunits)));
		break;
	    case Manual:
		request.setAttribute("manuals", ResearchResultPublication.sort(unit.getManuals(firstExecutionYear,
			finalExecutionYear, checkSubunits)));
		break;
	    case OtherPublication:
		request.setAttribute("other-publications", ResearchResultPublication.sort(unit.getOtherPublications(
			firstExecutionYear, finalExecutionYear, checkSubunits)));
		break;
	    case Proceedings:
		request.setAttribute("proceedings", ResearchResultPublication.sort(unit.getProceedings(firstExecutionYear,
			finalExecutionYear, checkSubunits)));
		break;
	    case TechnicalReport:
		request.setAttribute("technical-reports", ResearchResultPublication.sort(unit.getTechnicalReports(
			firstExecutionYear, finalExecutionYear, checkSubunits)));
		break;
	    case Thesis:
		request.setAttribute("theses", ResearchResultPublication.sort(unit.getTheses(firstExecutionYear,
			finalExecutionYear, checkSubunits)));
		break;
	    }
	}
    }

}
