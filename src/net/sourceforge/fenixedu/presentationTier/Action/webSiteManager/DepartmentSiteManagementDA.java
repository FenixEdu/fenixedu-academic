package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;

public class DepartmentSiteManagementDA extends CustomUnitSiteManagementDA {

    private Department getDepartment(final HttpServletRequest request) {
	DepartmentSite site = (DepartmentSite) getSite(request);
	return site == null ? null : site.getDepartment();
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
	return getDepartment(request).getRealName();
    }

    @Override
    protected String getRemoveManagerServiceName() {
	return "RemoveDepartmentSiteManager";
    }

    @Override
    protected String getAddManagerServiceName() {
	return "AddDepartmentSiteManager";
    }

}
