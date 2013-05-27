package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.credits.CreditsReportsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/exportCredits", scope = "request", parameter = "method")
public class DepartmentMemberCreditsReportsDA extends CreditsReportsDA {

    @Override
    public ActionForward prepareExportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException,  FenixServiceException {
        IUserView userView = UserView.getUser();
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        List<Department> availableDepartments = new ArrayList<Department>();
        availableDepartments.add(userView.getPerson().getTeacher().getCurrentWorkingDepartment());
        departmentCreditsBean.setAvailableDepartments(availableDepartments);
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("exportDepartmentCourses");
    }
}