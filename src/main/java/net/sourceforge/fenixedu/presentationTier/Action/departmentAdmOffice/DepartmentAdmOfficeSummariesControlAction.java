package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.SummariesControlAction;

import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/summariesControl", input = "/index.do", attribute = "summariesControlForm",
        formBean = "summariesControlForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "success", path = "/departmentAdmOffice/summariesControl/listTeacherSummariesControl.jsp",
        tileProperties = @Tile(title = "private.administrationofcreditsofdepartmentteachers.consultations.controlbrief")) })
public class DepartmentAdmOfficeSummariesControlAction extends SummariesControlAction {

    @Override
    protected void readAndSaveAllDepartments(HttpServletRequest request) throws  FenixServiceException {

        List<LabelValueBean> departments = new ArrayList<LabelValueBean>();
        final IUserView userView = UserView.getUser();
        Person person = userView.getPerson();
        List<Department> manageableDepartments = person.getManageableDepartmentCredits();
        for (Department department : manageableDepartments) {
            LabelValueBean bean = new LabelValueBean();
            bean.setLabel(department.getRealName());
            bean.setValue(department.getExternalId().toString());
            departments.add(bean);
        }
        request.setAttribute("departments", departments);
    }

}
