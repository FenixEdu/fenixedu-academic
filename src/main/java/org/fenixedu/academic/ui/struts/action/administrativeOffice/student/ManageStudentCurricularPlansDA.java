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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

@Mapping(path = "/manageStudentCurricularPlans", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "viewRegistrationDetails", path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp"),
        @Forward(name = "create",
                path = "/academicAdminOffice/student/registration/manageStudentCurricularPlans/createStudentCurricularPlan.jsp"),
        @Forward(name = "edit",
                path = "/academicAdminOffice/student/registration/manageStudentCurricularPlans/editStudentCurricularPlan.jsp")
})
public class ManageStudentCurricularPlansDA extends FenixDispatchAction {

    @SuppressWarnings("serial")
    public static class StudentCurricularPlanBean implements Serializable {

        private Registration registration;

        private DegreeCurricularPlan degreeCurricularPlan;

        private StudentCurricularPlan studentCurricularPlan;

        private ExecutionInterval executionInterval;

        private LocalDate startDate;

        public StudentCurricularPlanBean(final Registration registration) {
            this.registration = registration;
        }

        public StudentCurricularPlanBean(final StudentCurricularPlan studentCurricularPlan) {
            this.studentCurricularPlan = studentCurricularPlan;
            this.registration = studentCurricularPlan.getRegistration();
            this.degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
            this.startDate = studentCurricularPlan.getStartDateYearMonthDay().toLocalDate();
        }

        public Registration getRegistration() {
            return registration;
        }

        public void setRegistration(Registration registration) {
            this.registration = registration;
        }

        public DegreeCurricularPlan getDegreeCurricularPlan() {
            return degreeCurricularPlan;
        }

        public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
            this.degreeCurricularPlan = degreeCurricularPlan;
        }

        public StudentCurricularPlan getStudentCurricularPlan() {
            return studentCurricularPlan;
        }

        public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
            this.studentCurricularPlan = studentCurricularPlan;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public ExecutionInterval getExecutionInterval() {
            return executionInterval;
        }

        public void setExecutionInterval(ExecutionInterval executionInterval) {
            this.executionInterval = executionInterval;
        }

        public Collection<? extends ExecutionInterval> getExecutionIntervalOptions() {
            return ExecutionYear.readExecutionYears(getRegistration().getStartExecutionYear(),
                    ExecutionYear.readLastExecutionYear());
        }

        public Collection<DegreeCurricularPlan> getDegreeCurricularPlanOptions() {
            return getRegistration().getDegree().getDegreeCurricularPlansSet();
        }

    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("registration", getRegistration(request));

        return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentCurricularPlanBean bean = new StudentCurricularPlanBean(getRegistration(request));
        request.setAttribute("studentCurricularPlanBean", bean);

        return mapping.findForward("create");
    }

    public ActionForward prepareCreateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studentCurricularPlanBean", getRenderedObject("studentCurricularPlanBean"));

        return mapping.findForward("create");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final StudentCurricularPlanBean bean = getRenderedObject("studentCurricularPlanBean");

        try {
            createService(bean);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("studentCurricularPlanBean", bean);

            return mapping.findForward("create");
        }

        request.setAttribute("registrationId", bean.getRegistration().getExternalId());

        return list(mapping, form, request, response);
    }

    @Atomic
    private void createService(final StudentCurricularPlanBean bean) {
        bean.getRegistration().createStudentCurricularPlan(bean.getDegreeCurricularPlan(),
                ExecutionInterval.assertExecutionIntervalType(ExecutionYear.class, bean.getExecutionInterval()));
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        final Registration registration = studentCurricularPlan.getRegistration();

        try {
            deleteService(studentCurricularPlan);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        request.setAttribute("registrationId", registration.getExternalId());

        return list(mapping, form, request, response);
    }

    @Atomic
    private void deleteService(StudentCurricularPlan studentCurricularPlan) {
        studentCurricularPlan.delete();
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentCurricularPlanBean bean = new StudentCurricularPlanBean(getStudentCurricularPlan(request));

        request.setAttribute("studentCurricularPlanBean", bean);

        return mapping.findForward("edit");
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studentCurricularPlanBean", getRenderedObject("studentCurricularPlanBean"));

        return mapping.findForward("edit");

    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final StudentCurricularPlanBean bean = getRenderedObject("studentCurricularPlanBean");
        try {
            editService(bean);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("studentCurricularPlanBean", bean);

            return mapping.findForward("edit");
        }

        request.setAttribute("registrationId", bean.getRegistration().getExternalId());

        return list(mapping, form, request, response);
    }

    @Atomic
    private void editService(StudentCurricularPlanBean bean) {
        bean.getStudentCurricularPlan().setStartDate(new YearMonthDay(bean.getStartDate()));
    }

    private StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
        return getDomainObject(request, "studentCurricularPlanId");
    }

    private Registration getRegistration(HttpServletRequest request) {
        return getDomainObject(request, "registrationId");
    }

}
