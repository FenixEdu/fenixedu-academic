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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.RemoveDegreeFromDepartment;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Department.DepartmentDegreeBean;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminDCPApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AcademicAdminDCPApp.class, path = "department-degrees", titleKey = "label.manage.department.degrees",
        accessGroup = "academic(MANAGE_DEGREE_CURRICULAR_PLANS)")
@Mapping(module = "academicAdministration", path = "/manageDepartmentDegrees",
        input = "/manageDepartmentDegrees.do?method=prepare")
@Forwards(@Forward(name = "manageDepartmentDegrees", path = "/academicAdministration/department/manageDepartmentDegrees.jsp"))
public class DepartmentDegreesDAForAcademicAdministration extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        DepartmentDegreeBean departmentDegreeBean = getRenderedObject();
        if (departmentDegreeBean == null) {
            departmentDegreeBean = new DepartmentDegreeBean();
        }
        return forwardToPage(mapping, request, departmentDegreeBean);
    }

    public ActionForward associate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        executeFactoryMethod();
        return prepare(mapping, form, request, response);
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {
        final String departmentString = request.getParameter("departmentID");
        final String degreeString = request.getParameter("degreeID");
        final Department department = FenixFramework.getDomainObject(departmentString);
        final Degree degree = FenixFramework.getDomainObject(degreeString);
        RemoveDegreeFromDepartment.run(department, degree);
        final DepartmentDegreeBean departmentDegreeBean = new DepartmentDegreeBean();
        departmentDegreeBean.setDepartment(department);
        return forwardToPage(mapping, request, departmentDegreeBean);
    }

    private ActionForward forwardToPage(final ActionMapping mapping, final HttpServletRequest request,
            final DepartmentDegreeBean departmentDegreeBean) {
        request.setAttribute("departmentDegreeBean", departmentDegreeBean);
        return mapping.findForward("manageDepartmentDegrees");
    }

}
