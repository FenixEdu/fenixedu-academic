package net.sourceforge.fenixedu.presentationTier.Action.manager.student;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.ICalStudentTimeTable;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/calendarDebug", module = "manager")
@Forwards(@Forward(name = "CalendarData", path = "/manager/calendarData.jsp"))
public class CalendarDebugDA extends FenixDispatchAction {
    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String info = request.getParameter("user");
        if (info != null){
            User u = User.readUserByUserUId(info);
            HashMap<Registration, String> hm = new HashMap<>();
            if (u.getPerson().getStudent() != null){                
                for (Registration registration : u.getPerson().getStudent().getActiveRegistrations()) {
                    hm.put(registration, "<b>Aulas: </b>" + StringEscapeUtils.escapeHtml(ICalStudentTimeTable.getUrl("syncClasses", registration, request)) + "<br/>" + "<b>Exames: </b>" + StringEscapeUtils.escapeHtml(ICalStudentTimeTable.getUrl("syncExams", registration, request)) + "<br/>");  
                }
            }
            request.setAttribute("list", hm);
        }else{
            info = "";
        }
        
        request.setAttribute("user", info);
        return mapping.findForward("CalendarData");
    }
}
