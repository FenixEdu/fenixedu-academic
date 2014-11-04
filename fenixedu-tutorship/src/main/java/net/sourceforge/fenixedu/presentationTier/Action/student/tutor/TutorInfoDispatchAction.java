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
package org.fenixedu.academic.ui.struts.action.student.tutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.student.ExecutionPeriodStatisticsBean;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentViewApp.class, path = "tutor-info", titleKey = "link.student.tutorInfo")
@Mapping(module = "student", path = "/viewTutorInfo")
@Forwards(@Forward(name = "ShowStudentTutorInfo", path = "/student/tutor/showStudentTutorInfo.jsp"))
public class TutorInfoDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getLoggedPerson(request);
        List<Tutorship> pastTutors = new ArrayList<Tutorship>();

        Collection<Registration> registrations = person.getStudent().getRegistrationsSet();

        for (Registration registration : registrations) {
            Collection<StudentCurricularPlan> studentCurricularPlans = registration.getStudentCurricularPlansSet();
            for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
                for (Tutorship tutorship : studentCurricularPlan.getTutorshipsSet()) {
                    if (tutorship.isActive()) {
                        request.setAttribute("actualTutor", tutorship);
                        request.setAttribute("personID", tutorship.getTeacher().getPerson().getUsername());
                    } else {
                        pastTutors.add(tutorship);
                    }
                }
            }
        }
        request.setAttribute("pastTutors", pastTutors);
        return prepareStudentStatistics(mapping, actionForm, request, response);
    }

    public ActionForward prepareStudentStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getLoggedPerson(request);
        final Collection<Registration> registrations = person.getStudent().getRegistrationsSet();

        List<ExecutionPeriodStatisticsBean> studentStatistics = getStudentStatistics(registrations);

        request.setAttribute("studentStatistics", studentStatistics);
        return mapping.findForward("ShowStudentTutorInfo");
    }

    /*
     * AUXIALIRY METHODS
     */

    private List<ExecutionPeriodStatisticsBean> getStudentStatistics(Collection<Registration> registrations) {
        List<ExecutionPeriodStatisticsBean> studentStatistics = new ArrayList<ExecutionPeriodStatisticsBean>();

        Map<ExecutionSemester, ExecutionPeriodStatisticsBean> enrolmentsByExecutionPeriod =
                new HashMap<ExecutionSemester, ExecutionPeriodStatisticsBean>();

        for (Registration registration : registrations) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                for (ExecutionSemester executionSemester : studentCurricularPlan.getEnrolmentsExecutionPeriods()) {
                    if (enrolmentsByExecutionPeriod.containsKey(executionSemester)) {
                        ExecutionPeriodStatisticsBean executionPeriodStatisticsBean =
                                enrolmentsByExecutionPeriod.get(executionSemester);
                        executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan
                                .getEnrolmentsByExecutionPeriod(executionSemester));
                        enrolmentsByExecutionPeriod.put(executionSemester, executionPeriodStatisticsBean);
                    } else {
                        ExecutionPeriodStatisticsBean executionPeriodStatisticsBean =
                                new ExecutionPeriodStatisticsBean(executionSemester);
                        executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan
                                .getEnrolmentsByExecutionPeriod(executionSemester));
                        enrolmentsByExecutionPeriod.put(executionSemester, executionPeriodStatisticsBean);
                    }
                }
            }
        }

        studentStatistics.addAll(enrolmentsByExecutionPeriod.values());

        return studentStatistics;
    }
}
