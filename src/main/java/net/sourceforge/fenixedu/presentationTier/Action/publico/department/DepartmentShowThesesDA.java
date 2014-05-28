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
package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.publico.PublicShowThesesDA;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ThesisFilterBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/department/theses", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showThesisDetails", path = "department-showDegreeThesisDetails"),
        @Forward(name = "showTheses", path = "department-showDegreeTheses") })
public class DepartmentShowThesesDA extends PublicShowThesesDA {

    private Unit getUnit(HttpServletRequest request) {
        UnitSite site = SiteMapper.getSite(request);

        if (site == null) {
            String unitId = FenixContextDispatchAction.getFromRequest("selectedDepartmentUnitID", request);
            Unit unit = FenixFramework.getDomainObject(unitId);
            site = unit.getSite();
            OldCmsSemanticURLHandler.selectSite(request, site);
        }

        return site.getUnit();
    }

    private Department getDepartment(HttpServletRequest request) {
        Unit unit = getUnit(request);
        if (unit == null) {
            return null;
        } else {
            return unit.getDepartment();
        }
    }

    @Override
    protected ThesisFilterBean getFilterBean(HttpServletRequest request) throws Exception {
        ThesisFilterBean bean = super.getFilterBean(request);

        Department department = getDepartment(request);

        bean.setDepartment(department);
        bean.setDegreeOptions(new ArrayList<Degree>(department.getDegrees()));

        return bean;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("unit", getUnit(request));
        request.setAttribute("department", getDepartment(request));

        return super.execute(mapping, actionForm, request, response);
    }

}
