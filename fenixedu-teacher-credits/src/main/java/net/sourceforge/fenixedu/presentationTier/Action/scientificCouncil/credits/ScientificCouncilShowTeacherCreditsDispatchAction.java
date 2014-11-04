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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.credits;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.credits.CreditLineDTO;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherCredits;
import org.fenixedu.academic.ui.struts.action.credits.ShowTeacherCreditsDispatchAction;
import org.fenixedu.academic.ui.struts.action.credits.scientificCouncil.ScientificCouncilViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "scientificCouncil", path = "/showFullTeacherCreditsSheet", formBean = "teacherCreditsSheetForm",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards({
        @Forward(name = "show-teacher-credits", path = "/scientificCouncil/credits/showTeacherCredits.jsp"),
        @Forward(name = "teacher-not-found",
                path = "/scientificCouncil/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
public class ScientificCouncilShowTeacherCreditsDispatchAction extends ShowTeacherCreditsDispatchAction {

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, ParseException {

        InfoTeacherCredits infoTeacherCredits = new InfoTeacherCredits(form, request);
        Teacher teacher = infoTeacherCredits.getTeacher();
        ExecutionSemester executionSemester = infoTeacherCredits.getExecutionSemester();

        if (teacher == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }
        if (TeacherCredits.readTeacherCredits(executionSemester, teacher) != null
                && TeacherCredits.readTeacherCredits(executionSemester, teacher).getTeacherCreditsState().isCloseState()) {
            request.setAttribute("simulateCalc", "true");
        }
        getAllTeacherCredits(request, executionSemester, teacher);
        return mapping.findForward("show-teacher-credits");
    }

    public ActionForward simulateCalcTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ParseException {
        InfoTeacherCredits infoTeacherCredits = new InfoTeacherCredits(form, request);
        Teacher teacher = infoTeacherCredits.getTeacher();
        ExecutionSemester executionSemester = infoTeacherCredits.getExecutionSemester();
        getRequestAllTeacherCredits(request, executionSemester, teacher);
        CreditLineDTO creditLineDTO = simulateCalcCreditLine(teacher, executionSemester);
        request.setAttribute("simulateCalc", "false");
        request.setAttribute("creditLineDTO", creditLineDTO);
        return mapping.findForward("show-teacher-credits");
    }

    public ActionForward editTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, ParseException {
        InfoTeacherCredits infoTeacherCredits = new InfoTeacherCredits(form, request);
        Teacher teacher = infoTeacherCredits.getTeacher();
        ExecutionSemester executionSemester = infoTeacherCredits.getExecutionSemester();
        TeacherCredits.readTeacherCredits(executionSemester, teacher).editTeacherCredits(executionSemester);
        getAllTeacherCredits(request, executionSemester, teacher);
        request.setAttribute("simulateCalc", "true");
        return mapping.findForward("show-teacher-credits");
    }

    private ExecutionSemester getExecutionSemesterFromRequestOrForm(HttpServletRequest request, DynaActionForm teacherCreditsForm) {
        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject((String) teacherCreditsForm.get("executionPeriodId"));
        if (executionSemester != null) {
            return executionSemester;
        }
        return getDomainObject(request, "executionPeriodId");
    }

    private class InfoTeacherCredits {
        private final Teacher teacher;
        private final ExecutionSemester executionSemester;

        public InfoTeacherCredits(ActionForm form, HttpServletRequest request) {
            DynaActionForm teacherCreditsForm = (DynaActionForm) form;
            executionSemester = getExecutionSemesterFromRequestOrForm(request, teacherCreditsForm);
            teacher = FenixFramework.getDomainObject((String) teacherCreditsForm.get("teacherId"));
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }
    }
}
