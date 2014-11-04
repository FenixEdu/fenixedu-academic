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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ImprovementBolonhaStudentEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/improvementBolonhaStudentEnrollment", module = "academicAdministration",
        formBean = "bolonhaStudentEnrollmentForm", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showDegreeModulesToEnrol",
        path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp") })
public class AcademicAdminOfficeImprovementBolonhaStudentEnrolmentDA extends AcademicAdminOfficeBolonhaStudentEnrollmentDA {

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
        return CurricularRuleLevel.IMPROVEMENT_ENROLMENT;
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
        request.setAttribute("action", getAction());
        request.setAttribute("bolonhaStudentEnrollmentBean", new ImprovementBolonhaStudentEnrolmentBean(studentCurricularPlan,
                executionSemester));

        addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionSemester, request);

        return mapping.findForward("showDegreeModulesToEnrol");
    }

    @Override
    protected String getAction() {
        return "/improvementBolonhaStudentEnrollment.do";
    }

}
