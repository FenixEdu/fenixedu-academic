package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/showDegreeAnnouncements", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "viewAnnouncement", path = "degree-view-announcement"),
        @Forward(name = "listAnnouncements", path = "public-degree-list-announcements") })
public class DegreeAnnouncementsDA extends AnnouncementManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setDegreeContext(request);
        setPageLanguage(request);

        return super.execute(mapping, actionForm, request, response);
    }

    private void setDegreeContext(HttpServletRequest request) {
        Degree degree = getDegree(request);
        if (degree != null) {
            request.setAttribute("degree", degree);
        }

        Collection<Campus> campus = degree.getCurrentCampus();
        if (campus.isEmpty()) {
            campus = degree.getCurrentCampus();
        }
        request.setAttribute("campus", campus);
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

    public Degree getDegree(HttpServletRequest request) {
        String parameter = request.getParameter("degreeID");

        if (parameter == null) {
            return null;
        }
        try {
            return FenixFramework.getDomainObject(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Unit getUnit(HttpServletRequest request) {
        Degree degree = getDegree(request);
        return degree == null ? null : degree.getUnit();
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();

        addExtraParameter(request, builder, "degreeID");

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
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/showDegreeAnnouncements.do";
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        Unit unit = getUnit(request);
        List<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();

        if (unit == null) {
            return boards;
        }

        User userView = getUserView(request);
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
            for (AnnouncementBoard board : unit.getBoards()) {
                if (board.getReaders() == null) {
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

}
