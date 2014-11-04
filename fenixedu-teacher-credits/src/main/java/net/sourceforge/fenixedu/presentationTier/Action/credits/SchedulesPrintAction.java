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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.util.WeekDay;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.FenixFramework;

public class SchedulesPrintAction extends ShowTeacherCreditsDispatchAction {

    public ActionForward showSchedulesPrint(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String executionPeriodId = request.getParameter("executionPeriodId");
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        Teacher teacher = FenixFramework.getDomainObject(request.getParameter("teacherId"));

        getAllTeacherCredits(request, executionSemester, teacher);

        OccupationPeriod occupationPeriod = executionSemester.getLessonsPeriod();
        setLegalRegimen(request, occupationPeriod, teacher);
        setWorkingUnit(request, occupationPeriod, teacher);
        setWeekDays(request);

        request.setAttribute("teacher", teacher);
        request.setAttribute("teacherService", TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester));

        return mapping.findForward("show-schedules-resume-print");
    }

    private void setWeekDays(HttpServletRequest request) {
        List<WeekDay> weekDays = new ArrayList<WeekDay>();
        for (WeekDay weekDay : WeekDay.values()) {
            if (!weekDay.equals(WeekDay.SUNDAY)) {
                weekDays.add(weekDay);
            }
        }
        request.setAttribute("weekDays", weekDays);
    }

    private void setWorkingUnit(HttpServletRequest request, OccupationPeriod occupationPeriod, Teacher teacher) {
        if (occupationPeriod != null) {
            Unit lastWorkingUnit =
                    teacher.getPerson().getEmployee() != null ? teacher.getPerson().getEmployee().getLastWorkingPlace(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay()) : null;
            if (lastWorkingUnit != null) {
                request.setAttribute("workingUnit", lastWorkingUnit);
                Unit departmentUnit = lastWorkingUnit.getDepartmentUnit();
                if (departmentUnit != null && departmentUnit.getDepartment() != null) {
                    request.setAttribute("departmentRealName", lastWorkingUnit.getDepartmentUnit().getDepartment().getRealName());
                }
            }
        }
    }

    private void setLegalRegimen(HttpServletRequest request, OccupationPeriod occupationPeriod, Teacher teacher) {
        if (occupationPeriod != null) {
            PersonContractSituation teacherContractSituation =
                    PersonContractSituation.getCurrentOrLastTeacherContractSituation(teacher, occupationPeriod.getStartYearMonthDay().toLocalDate(), occupationPeriod.getEndYearMonthDay().toLocalDate());
            if (teacherContractSituation != null) {
                request.setAttribute("teacherContractSituation", teacherContractSituation);
            }
        }
    }
}
