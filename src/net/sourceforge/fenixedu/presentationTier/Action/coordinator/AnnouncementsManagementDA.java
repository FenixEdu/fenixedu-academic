package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AnnouncementsManagementDA extends AnnouncementManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlan.getIdInternal());
        }

        return super.execute(mapping, actionForm, request, response);
    }

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        String parameter = request.getParameter("degreeCurricularPlanID");
        
        if (parameter == null) {
            return null;
        }
        
        try {
            Integer oid = new Integer(parameter);
            return RootDomainObject.getInstance().readDegreeCurricularPlanByOID(oid);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public Unit getUnit(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        return degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree().getUnit();
    }
    
    public ActionForward viewBoards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        Unit unit = getUnit(request);
        
        if (unit == null || unit.getBoards().isEmpty()) {
            return mapping.findForward("noBoards");
        }
        else {
            List<PartyAnnouncementBoard> boards = unit.getBoards();
            if (boards.size() > 1) {
                return start(mapping, actionForm, request, response);
            }
            else {
                AnnouncementBoard board = boards.get(0);
                
                ActionForward forward = new ActionForward(mapping.findForward("viewAnnouncementsRedirect"));
                forward.setPath(forward.getPath() + String.format("&degreeCurricularPlanID=%s&announcementBoardId=%s", degreeCurricularPlan.getIdInternal(), board.getIdInternal()));
                forward.setRedirect(true);
                
                return forward;
            }
        }
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        
        addExtraParameter(request, builder, "degreeCurricularPlanID");
        addExtraParameter(request, builder, "tabularVersion");
        
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
    protected String getContextInformation(HttpServletRequest request) {
        return "/announcementsManagement.do";
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        Unit unit = getUnit(request);
        
        Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
        if (unit != null) {
            for (AnnouncementBoard board : unit.getBoards()) {
                if (board.getWriters() == null || board.getReaders() == null || board.getManagers() == null
                        || board.getWriters().allows(getUserView(request))
                        || board.getReaders().allows(getUserView(request))
                        || board.getManagers().allows(getUserView(request)))
                    boards.add(board);
            }

        }
        
        return boards;
    }

    @Override
    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");
        
        return super.addAnnouncement(mapping, form, request, response);
    }

    @Override
    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");
        
        return super.editAnnouncement(mapping, form, request, response);
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");
        
        return super.viewAnnouncements(mapping, form, request, response);
    }
    
}
