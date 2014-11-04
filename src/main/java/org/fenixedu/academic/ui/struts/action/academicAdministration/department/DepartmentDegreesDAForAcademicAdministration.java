/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.academicAdministration.department;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.RemoveDegreeFromDepartment;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminDCPApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AcademicAdminDCPApp.class, path = "department-degrees", titleKey = "label.manage.department.degrees",
        accessGroup = "academic(MANAGE_DEGREE_CURRICULAR_PLANS)")
@Mapping(module = "academicAdministration", path = "/manageDepartmentDegrees",
        input = "/manageDepartmentDegrees.do?method=prepare")
@Forwards(@Forward(name = "manageDepartmentDegrees", path = "/academicAdministration/department/manageDepartmentDegrees.jsp"))
public class DepartmentDegreesDAForAcademicAdministration extends FenixDispatchAction {

    public static class DepartmentDegreeBean implements FactoryExecutor, Serializable {

        private Department department;
        private Degree degree;

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(final Department department) {
            this.department = department;
        }

        public Degree getDegree() {
            return degree;
        }

        public void setDegree(final Degree degree) {
            this.degree = degree;
        }

        @Override
        public Object execute() {
            final Department department = getDepartment();
            final Degree degree = getDegree();
            if (department != null && degree != null) {
                department.getDegreesSet().add(degree);
            }
            return null;
        }
    }

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
