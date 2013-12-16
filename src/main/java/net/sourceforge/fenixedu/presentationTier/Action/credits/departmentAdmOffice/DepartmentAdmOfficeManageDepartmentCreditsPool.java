package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import java.util.ArrayList;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDepartmentCreditsPool;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/creditsPool", scope = "request", parameter = "method")
public class DepartmentAdmOfficeManageDepartmentCreditsPool extends ManageDepartmentCreditsPool {

    @Override
    protected DepartmentCreditsBean getDepartmentCreditsBean() {
        User userView = Authenticate.getUser();
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(userView.getPerson()
                .getManageableDepartmentCredits()));
        return departmentCreditsBean;
    }
}
