package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.ManageUnitSiteManagers;

public class DepartmentSiteDA extends ManageUnitSiteManagers {

    private Department getDepartment(final HttpServletRequest request) {
        return getUserView(request).getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }
	
    @Override
    protected DepartmentSite getSite(HttpServletRequest request) {
        Department department = getDepartment(request);
        
        Unit departmentUnit = department.getDepartmentUnit();
        if (departmentUnit == null) {
            return null;
        }
        
        return (DepartmentSite) departmentUnit.getSite();
    }

    @Override
	protected String getRemoveServiceName() {
		return "RemoveDepartmentSiteManager";
	}

    @Override
	protected String getAddServiceName() {
		return "AddDepartmentSiteManager";
	}
    
}
