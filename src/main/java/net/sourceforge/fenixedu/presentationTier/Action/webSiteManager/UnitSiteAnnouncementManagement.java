package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UnitSiteAnnouncementManagement extends AnnouncementManagement {

    private static final Logger logger = LoggerFactory.getLogger(UnitSiteAnnouncementManagement.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("site", getSite(request));
        return super.execute(mapping, actionForm, request, response);
    }

    private Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    protected UnitSite getSite(HttpServletRequest request) {
        return getDomainObject(request, "oid");
    }

    protected Unit getUnit(HttpServletRequest request) {
        UnitSite site = getSite(request);
        if (site == null) {
            return null;
        } else {
            return site.getUnit();
        }
    }

    public ActionForward viewBoards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        Unit unit = site.getUnit();

        if (unit == null || unit.getBoards().isEmpty()) {
            return mapping.findForward("noBoards");
        } else {
            Collection<PartyAnnouncementBoard> boards = unit.getBoards();
            if (boards.size() > 1) {
                return start(mapping, actionForm, request, response);
            } else {
                AnnouncementBoard board = boards.iterator().next();

                ActionForward forward = new ActionForward(mapping.findForward("viewAnnouncementsRedirect"));
                forward.setPath(forward.getPath()
                        + String.format("&announcementBoardId=%s&oid=", board.getExternalId(), site.getExternalId()));
                forward.setRedirect(true);

                return forward;
            }
        }
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();

        addExtraParameter(request, builder, "tabularVersion");
        addExtraParameter(request, builder, "oid");

        return builder.toString();
    }

    protected void addExtraParameter(HttpServletRequest request, StringBuilder builder, String name) {
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

        Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
        if (unit != null) {
            for (AnnouncementBoard board : unit.getBoards()) {
                if (board.getWriters() == null || board.getReaders() == null || board.getManagers() == null
                        || board.getWriters().allows(getUserView(request)) || board.getReaders().allows(getUserView(request))
                        || board.getManagers().allows(getUserView(request))) {
                    boards.add(board);
                }
            }

        }

        return boards;
    }

    @Override
    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");

        return super.addAnnouncement(mapping, form, request, response);
    }

    @Override
    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");

        return super.editAnnouncement(mapping, form, request, response);
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");

        return super.viewAnnouncements(mapping, form, request, response);
    }

}
