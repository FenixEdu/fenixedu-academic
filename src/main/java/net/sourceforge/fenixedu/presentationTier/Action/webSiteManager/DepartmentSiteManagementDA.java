package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.site.AddUnitSiteManager;
import net.sourceforge.fenixedu.applicationTier.Servico.site.RemoveUnitSiteManager;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/manageDepartmentSite", functionality = ListSitesAction.class)
public class DepartmentSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected void setContext(HttpServletRequest request) {
        request.setAttribute("siteActionName", "/manageDepartmentSite.do");
        request.setAttribute("siteContextParam", "oid");
        request.setAttribute("siteContextParamValue", getSite(request).getExternalId());
        request.setAttribute("announcementsActionName", "/manageDepartmentSiteAnnouncements.do");
        request.setAttribute("unitId", getSite(request).getUnit().getExternalId());
    }

    private Department getDepartment(final HttpServletRequest request) {
        DepartmentSite site = (DepartmentSite) getSite(request);
        return site == null ? null : site.getDepartment();
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getDepartment(request).getRealName();
    }

    @Override
    protected void removeUnitSiteManager(UnitSite site, Person person) throws FenixServiceException {
        RemoveUnitSiteManager.runRemoveDepartmentSiteManager(site, person);
    }

    @Override
    protected void addUnitSiteManager(UnitSite site, Person person) throws FenixServiceException {
        AddUnitSiteManager.runAddDepartmentSiteManager(site, person);
    }

}
