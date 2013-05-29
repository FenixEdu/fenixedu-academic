package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDepartmentCreditsPool;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/creditsPool", scope = "request", parameter = "method")
public class ScientificCouncilManageDepartmentCreditsPool extends ManageDepartmentCreditsPool {

    @Override
    protected DepartmentCreditsBean getDepartmentCreditsBean() {
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(rootDomainObject.getDepartments()));
        return departmentCreditsBean;
    }

}
