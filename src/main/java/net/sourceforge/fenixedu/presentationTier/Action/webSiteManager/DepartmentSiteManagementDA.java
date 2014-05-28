/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
