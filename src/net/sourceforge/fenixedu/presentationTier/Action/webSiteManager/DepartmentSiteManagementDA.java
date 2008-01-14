package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Item;

public class DepartmentSiteManagementDA extends CustomUnitSiteManagementDA {

    private Department getDepartment(final HttpServletRequest request) {
        DepartmentSite site = (DepartmentSite) getSite(request);
        return site == null ? null : site.getDepartment();
    }
    
    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
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
