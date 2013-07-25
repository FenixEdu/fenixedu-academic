package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/courseStatistics", module = "departmentMember")
@Forwards({ @Forward(name = "showCompetenceCourses", path = "/departmentMember/courseStatistics/viewStatistics.jsp")//,
      //  @Forward(name = "viewForum", path = "/commons/forums/viewForum.jsp"),
        //@Forward(name = "createThreadAndMessage", path = "/commons/forums/createThreadAndMessage.jsp"),
        /*@Forward(name = "viewThread", path = "/commons/forums/viewThread.jsp")*/ })
public class CourseStatisticsDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        //request.setAttribute("contextPrefix", "/departmentForum.do");
        //request.setAttribute("module", "/departmentMember");
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException,  FenixServiceException {
    /*    List<DepartmentForum> foruns = new ArrayList<DepartmentForum>();
        Person loggedPerson = getLoggedPerson(request);
        for (Department department : rootDomainObject.getDepartments()) {
            //if (belongsPersonWithDepartment(loggedPerson, department)) {
                foruns.add(department.getDepartmentForum());
            //}
        }

        if (foruns.size() == 1) {
            request.setAttribute("forum", foruns.get(0));
            return viewForum(mapping, form, request, response);
        }

        request.setAttribute("foruns", foruns);
        return mapping.findForward("viewDepartmentForum");*/
    	return mapping.findForward("showCompetenceCourses");
    }
}
