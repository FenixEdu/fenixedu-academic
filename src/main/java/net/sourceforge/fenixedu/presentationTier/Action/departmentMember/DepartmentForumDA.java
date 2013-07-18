package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.DepartmentForum;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.ForunsManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/departmentForum", module = "departmentMember")
@Forwards({ @Forward(name = "viewDepartmentForum", path = "/departmentMember/forum/viewDepartmentForum.jsp"),
        @Forward(name = "viewForum", path = "/commons/forums/viewForum.jsp"),
        @Forward(name = "createThreadAndMessage", path = "/commons/forums/createThreadAndMessage.jsp"),
        @Forward(name = "viewThread", path = "/commons/forums/viewThread.jsp") })
public class DepartmentForumDA extends ForunsManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("contextPrefix", "/departmentForum.do");
        request.setAttribute("module", "/departmentMember");
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException,  FenixServiceException {
        List<DepartmentForum> foruns = new ArrayList<DepartmentForum>();
        Person loggedPerson = getLoggedPerson(request);
        for (Department department : rootDomainObject.getDepartments()) {
            if (belongsPersonWithDepartment(loggedPerson, department)) {
                foruns.add(department.getDepartmentForum());
            }
        }

        if (foruns.size() == 1) {
            request.setAttribute("forum", foruns.get(0));
            return viewForum(mapping, form, request, response);
        }

        request.setAttribute("foruns", foruns);
        return mapping.findForward("viewDepartmentForum");
    }

    @Override
    protected Forum getRequestedForum(HttpServletRequest request) {
        String forumId = request.getParameter("forumId");
        if (forumId == null) {
            return (Forum) request.getAttribute("forum");
        }
        return super.getRequestedForum(request);
    }

    private boolean belongsPersonWithDepartment(Person person, Department department) {
        DepartmentForum departmentForum = department.getDepartmentForum();
        if (departmentForum != null) {
            return departmentForum.getDepartmentForumGroup().isMember(person);
        }
        return false;
    }
}
