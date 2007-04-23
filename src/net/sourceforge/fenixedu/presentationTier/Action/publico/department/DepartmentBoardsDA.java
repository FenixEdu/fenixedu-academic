package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class DepartmentBoardsDA extends AnnouncementManagement {
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setUnitContext(request);
        setPageLanguage(request);
        
        return super.execute(mapping, actionForm, request, response);
    }

    private void setUnitContext(HttpServletRequest request) {
        Department department = getDepartment(request);
        if (department != null) {
            request.setAttribute("department", department);
            request.setAttribute("unit", department.getDepartmentUnit());
        }
    }

    private void setPageLanguage(HttpServletRequest request) {
        Boolean inEnglish;
        
        String inEnglishParameter = request.getParameter("inEnglish");
        if (inEnglishParameter == null) {
            inEnglish = (Boolean) request.getAttribute("inEnglish");
        }
        else {
            inEnglish = new Boolean(inEnglishParameter);
        }
        
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        
        request.setAttribute("inEnglish", inEnglish);
    }

    public Department getDepartment(HttpServletRequest request) {
        Unit unit = getUnit(request);
        if (unit == null) {
            return null;
        }
        else {
            return unit.getDepartment();
        }
    }
    
    public Unit getUnit(HttpServletRequest request) {
        String parameter = request.getParameter("selectedDepartmentUnitID");
        
        if (parameter == null) {
            return null;
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
        
        addExtraParameter(request, builder, "selectedDepartmentUnitID");
        
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
        return "/department/announcements.do";
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
        }
        else {
            String name = getBoardName();
            for (AnnouncementBoard board : unit.getBoards()) {
                if (board.getReaders() == null && board.getName().equals(name)) {
                    return board;
                }
            }
            
            return null;
        }
    }

    protected abstract String getBoardName();
    
    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AnnouncementBoard board = getRequestedAnnouncementBoard(request);
        if (board != null) {
            return super.viewAnnouncements(mapping, form, request, response);
        }
        else {
            return mapping.findForward("listAnnouncements");
        }
    }
    
}
