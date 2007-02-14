package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PublicDepartmentSiteDA extends SiteVisualizationDA {

    public static int ANNOUNCEMENTS_NUMBER = 3;
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = getIntegerFromRequest(request, "selectedDepartmentUnitID");
        Unit unit = (Unit) RootDomainObject.getInstance().readPartyByOID(id);
        
        if (unit != null) {
            request.setAttribute("unit", unit);
            request.setAttribute("department", unit.getDepartment());
            
            DepartmentSite site = (DepartmentSite) unit.getSite();
            request.setAttribute("site", site);
            if (site.getDescription() == null || site.getDescription().getAllLanguages().isEmpty()) {
                request.setAttribute("noDescription", true);
            }

            AnnouncementBoard board = null;
            for (AnnouncementBoard unitBoard : unit.getBoards()) {
                if (unitBoard.isPublicToRead()) {
                    board = unitBoard;
                    break;
                }
            }
            
            if (board != null) {
                List<Announcement> announcements = board.getActiveAnnouncements();
                announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
                request.setAttribute("announcements", announcements);
            }
        }
        else {
            request.setAttribute("noDescription", true);
        }
        
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }

    public ActionForward presentation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("department-presentation");
    }
    
    private Unit getUnit(HttpServletRequest request) {
        return (Unit) request.getAttribute("unit");
    }
    
    public ActionForward employees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        Unit unit = getUnit(request);
        
        List<Employee> employees = unit.getAllCurrentNonTeacherEmployees();
        Collections.sort(employees, new Comparator<Employee>() {

            public int compare(Employee o1, Employee o2) {
                return o1.getPerson().getNickname().compareTo(o2.getPerson().getNickname());
            }
            
        });
        
        request.setAttribute("employees", employees);
        return mapping.findForward("department-employees");
    }
}
