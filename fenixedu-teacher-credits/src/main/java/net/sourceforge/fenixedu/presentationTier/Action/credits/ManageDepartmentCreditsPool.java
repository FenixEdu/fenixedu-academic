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
package org.fenixedu.academic.ui.struts.action.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.credits.util.DepartmentCreditsBean;
import org.fenixedu.academic.domain.credits.util.DepartmentCreditsPoolBean;
import org.fenixedu.academic.domain.credits.util.DepartmentCreditsPoolBean.DepartmentExecutionCourse;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@Forwards(@Forward(name = "manageUnitCredits", path = "/credits/creditsPool/manageUnitCredits.jsp"))
public class ManageDepartmentCreditsPool extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareManageUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("manageUnitCredits");
    }

    protected DepartmentCreditsBean getDepartmentCreditsBean() {
        User userView = Authenticate.getUser();
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(userView.getPerson()
                .getManageableDepartmentCreditsSet()));
        return departmentCreditsBean;
    }

    public ActionForward viewDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        if (departmentCreditsBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        DepartmentCreditsPoolBean departmentCreditsPoolBean = new DepartmentCreditsPoolBean(departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }

    public ActionForward postBackUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsPoolBean departmentCreditsPoolBean = getRenderedObject("departmentCreditsPoolBean");
        if (departmentCreditsPoolBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        departmentCreditsBean.setDepartment(departmentCreditsPoolBean.getDepartment());
        departmentCreditsBean.setExecutionYear(departmentCreditsPoolBean.getAnnualCreditsState().getExecutionYear());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }

    public ActionForward editUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsPoolBean departmentCreditsPoolBean = getRenderedObject("departmentCreditsPoolBean");
        RenderUtils.invalidateViewState();
        if (departmentCreditsPoolBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        try {
            departmentCreditsPoolBean.editUnitCredits();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        departmentCreditsBean.setDepartment(departmentCreditsPoolBean.getDepartment());
        departmentCreditsBean.setExecutionYear(departmentCreditsPoolBean.getAnnualCreditsState().getExecutionYear());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }

    public ActionForward exportDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        RenderUtils.invalidateViewState();
        if (departmentCreditsBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        DepartmentCreditsPoolBean departmentCreditsPoolBean = new DepartmentCreditsPoolBean(departmentCreditsBean);

        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();

        getExecutionCoursesSheet(departmentCreditsPoolBean.getDepartmentSharedExecutionCourses(), spreadsheet,
                "Disciplinas_Partilhadas");
        getExecutionCoursesSheet(departmentCreditsPoolBean.getOtherDepartmentSharedExecutionCourses(), spreadsheet,
                "Disciplinas_Partilhadas_Outros_Dep");
        getExecutionCoursesSheet(departmentCreditsPoolBean.getDepartmentExecutionCourses(), spreadsheet, "Restantes_Disciplinas");

        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename="
                + departmentCreditsPoolBean.getDepartment().getAcronym() + ".xls");
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    protected void getExecutionCoursesSheet(Set<DepartmentExecutionCourse> executionCourses, StyledExcelSpreadsheet spreadsheet,
            String sheetname) {
        spreadsheet.getSheet(sheetname);
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.course"), 10000);
        spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.degrees"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.execution-period"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.effortRate"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.departmentEffectiveLoad"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.totalEffectiveLoad"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.unitCredit"));
        for (DepartmentExecutionCourse departmentExecutionCourse : executionCourses) {
            spreadsheet.newRow();
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getName());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getDegreePresentationString());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getExecutionPeriod().getSemester());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getEffortRate());
            spreadsheet.addCell(departmentExecutionCourse.getDepartmentEffectiveLoad());
            spreadsheet.addCell(departmentExecutionCourse.getTotalEffectiveLoad());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getUnitCreditValue());
        }
    }
}
