package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class UnitSiteBoardsDA extends AnnouncementManagement {

    // TODO: change literal
    public static final MultiLanguageString ANNOUNCEMENTS = MultiLanguageString.i18n().add("pt", "Anúncios").finish();
    public static final MultiLanguageString EVENTS = MultiLanguageString.i18n().add("pt", "Eventos").finish();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setUnitContext(request);
	setPageLanguage(request);

	return super.execute(mapping, actionForm, request, response);
    }

    protected void setUnitContext(HttpServletRequest request) {
	Unit unit = getUnit(request);
	if (unit != null) {
	    request.setAttribute("unit", unit);
	    request.setAttribute("site", unit.getSite());
	}
    }

    private void setPageLanguage(HttpServletRequest request) {
	Boolean inEnglish;

	String inEnglishParameter = request.getParameter("inEnglish");
	if (inEnglishParameter == null) {
	    inEnglish = (Boolean) request.getAttribute("inEnglish");
	} else {
	    inEnglish = new Boolean(inEnglishParameter);
	}

	if (inEnglish == null) {
	    inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
	}

	request.setAttribute("inEnglish", inEnglish);
    }

    public Unit getUnit(HttpServletRequest request) {
	String parameter = request.getParameter(getContextParamName());

	if (parameter == null) {
	    UnitSite site = (UnitSite) FilterFunctionalityContext.getCurrentContext(request).getSelectedContainer();
	    return site.getUnit();
	}

	try {
	    Integer oid = new Integer(parameter);
	    return (Unit) RootDomainObject.getInstance().readPartyByOID(oid);
	} catch (NumberFormatException e) {
	    return null;
	}
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
	StringBuilder builder = new StringBuilder();

	addExtraParameter(request, builder, getContextParamName());

	return builder.toString();
    }

    private void addExtraParameter(HttpServletRequest request, StringBuilder builder, String name) {
	String parameter = request.getParameter(name);
	if (parameter != null) {
	    if (builder.length() != 0) {
		builder.append("&amp;");
	    }

	    builder.append(name + "=" + parameter);
	}
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
	Unit unit = getUnit(request);
	List<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();

	if (unit == null) {
	    return boards;
	}

	IUserView userView = getUserView(request);
	for (AnnouncementBoard board : unit.getBoards()) {
	    if (board.getReaders() == null) {
		boards.add(board);
	    }

	    if (board.getReaders().allows(userView)) {
		boards.add(board);
	    }
	}

	return boards;
    }

    @Override
    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
	Unit unit = getUnit(request);

	if (unit == null) {
	    return null;
	} else {
	    MultiLanguageString name = getBoardName(request);
	    for (AnnouncementBoard board : unit.getBoards()) {
		if (board.getReaders() == null && board.getName().equalInAnyLanguage(name)) {
		    return board;
		}
	    }

	    return null;
	}
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AnnouncementBoard board = getRequestedAnnouncementBoard(request);

	if (board != null) {
	    return super.viewAnnouncements(mapping, form, request, response);
	} else {
	    return mapping.findForward("listAnnouncements");
	}
    }

    public ActionForward viewEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return viewAnnouncement(mapping, form, request, response);
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
	String path = getActionPath(mapping, request);

	request.setAttribute("announcementActionVariable", path);

	if (isShowingAnnouncements(request)) {
	    request.setAttribute("showingAnnouncements", true);
	}

	if (isShowingEvents(request)) {
	    request.setAttribute("showingEvents", true);
	}

	return path;
    }

    protected boolean isShowingAnnouncements(HttpServletRequest request) {
	return getBoardName(request).equalInAnyLanguage(ANNOUNCEMENTS);
    }

    protected boolean isShowingEvents(HttpServletRequest request) {
	return getBoardName(request).equalInAnyLanguage(EVENTS);
    }

    public String getContextParamName() {
	return "unitID";
    }

    protected abstract MultiLanguageString getBoardName(HttpServletRequest request);

    protected String getActionPath(ActionMapping mapping, HttpServletRequest request) {
	return mapping.getPath() + ".do";
    }

}
