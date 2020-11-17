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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import org.fenixedu.academic.dto.student.ExecutionPeriodStatisticsBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.commons.student.ReadStudentCurricularPlan;
import org.fenixedu.academic.service.services.commons.student.ReadStudentCurriculum;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.renderers.student.curriculum.StudentCurricularPlanRenderer.EnrolmentStateFilterType;
import org.fenixedu.academic.ui.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType;
import org.fenixedu.academic.ui.renderers.student.curriculum.StudentCurricularPlanRenderer.ViewType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.academic.ui.struts.action.exceptions.ExistingActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.util.StudentCurricularPlanIDDomainType;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 * @author André Fernandes / João Brito
 */
@Mapping(path = "/viewStudentCurriculum", module = "academicAdministration",
        formBean = "studentCurricularPlanAndEnrollmentsSelectionForm", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "ShowStudentCurriculumForCoordinator", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized_bd.jsp") })
public class CurriculumDispatchAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(CurriculumDispatchAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        Registration registration = null;

        final String registrationOID = getRegistrationOID(request);
        final Student loggedStudent = getUserView(request).getPerson().getStudent();

        if (registrationOID != null) {
            registration = FenixFramework.getDomainObject(registrationOID);
        } else if (loggedStudent != null) {
            /**
             * We no longer want to filter students with 1 registration only.
             * All taskflows are now forced to render "chooseRegistration.jsp"
             * in order to make it possible to present the
             * ExtraCurricularActivities information. This old block of code is
             * kept commented only for legacy purpose.
             */
            // if (loggedStudent.getRegistrations().size() == 1) {
            // registration = loggedStudent.getRegistrations().iterator().next();
            // } else {
            request.setAttribute("student", loggedStudent);

            List<Registration> validRegistrations = getValidRegistrations(loggedStudent);

            request.setAttribute("validRegistrations", validRegistrations);

            return mapping.findForward("chooseRegistration");
            // }
        }

        if (registration == null) {
            return mapping.findForward("NotAuthorized");
        } else {
            return getStudentCP(registration, mapping, (DynaActionForm) form, request);
        }
    }

    private List<Registration> getValidRegistrations(Student loggedStudent) {
        List<Registration> result = new ArrayList<Registration>();

        for (Registration registration : loggedStudent.getRegistrationsSet()) {
            if (!registration.isCanceled()) {
                result.add(registration);
            }
        }

        return result;
    }

    private String getRegistrationOID(HttpServletRequest request) {
        String registrationOID = request.getParameter("registrationOID");
        if (registrationOID == null) {
            registrationOID = (String) request.getAttribute("registrationOID");
        }

        return (registrationOID == null || registrationOID.equals("")) ? null : registrationOID;
    }

    protected Registration getStudentRegistration(Student student, String degreeCurricularPlanId) {
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        return student.readRegistrationByDegreeCurricularPlan(degreeCurricularPlan);
    }

    public ActionForward prepareReadByStudentNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        DynaActionForm actionForm = (DynaActionForm) form;
        Registration registration = null;

        final String degreeCurricularPlanId = (String) actionForm.get("degreeCurricularPlanID");
        Student student = getStudent(actionForm);
        if (student != null) {
            if (!StringUtils.isEmpty(degreeCurricularPlanId)) {
                registration = getStudentRegistration(student, degreeCurricularPlanId);
            } else {
                final Collection<Registration> registrations = student.getRegistrationsSet();
                if (!registrations.isEmpty()) {
                    registration = registrations.iterator().next();
                }
            }
        }

        if (registration == null) {
            return mapping.findForward("NotAuthorized");
        } else {
            return getStudentCP(registration, mapping, actionForm, request);
        }

    }

    private Student getStudent(DynaActionForm form) {
        final Integer studentNumber = Integer.valueOf((String) form.get("studentNumber"));
        Student student = Student.readStudentByNumber(studentNumber);
        // if (student != null) {
        // Teacher teacher = AccessControl.getPerson().getTeacher();
        // for (Tutorship tutorship : student.getTutorships()) {
        // if (tutorship.getTeacher().equals(teacher))
        // return student;
        // }
        // for (Coordinator coordinator :
        // AccessControl.getPerson().getCoordinators()) {
        // DegreeCurricularPlan dcp =
        // coordinator.getExecutionDegree().getDegreeCurricularPlan();
        // for (Registration registration : student.getRegistrations()) {
        // if (registration.getStudentCurricularPlan(dcp) != null)
        // return student;
        // }
        // }
        // }
        // return null;
        return student;
    }

    protected ActionForward getStudentCPForSupervisor(final Registration registration, final ActionMapping mapping,
            DynaActionForm actionForm, final HttpServletRequest request) {

        return getStudentCP(registration, mapping, actionForm, request);
    }

    private ActionForward getStudentCP(final Registration registration, final ActionMapping mapping, DynaActionForm actionForm,
            final HttpServletRequest request) {
        request.setAttribute("registration", registration);

        String studentCPID = getStudentCPID(request, actionForm);
        if (StringUtils.isEmpty(studentCPID)) {
            studentCPID = getDefaultStudentCPID(registration).getId().toString();
            actionForm.set("studentCPID", studentCPID);
        }
        request.setAttribute("selectedStudentCurricularPlans", getSelectedStudentCurricularPlans(registration, studentCPID));
        request.setAttribute("scpsLabelValueBeanList", getSCPsLabelValueBeanList(registration.getStudentCurricularPlansSet()));

        if (StringUtils.isEmpty(actionForm.getString("viewType"))) {
            actionForm.set("viewType", ViewType.ALL.name());
        }

        if (StringUtils.isEmpty(actionForm.getString("select"))) {
            actionForm.set("select",
                    AcademicPredicates.VIEW_FULL_STUDENT_CURRICULUM
                            .evaluate(AccessControl.getPerson()) ? EnrolmentStateFilterType.ALL
                                    .name() : EnrolmentStateFilterType.APPROVED_OR_ENROLED.name());
        }

        if (StringUtils.isEmpty(actionForm.getString("organizedBy"))) {
            String organizedBy = OrganizationType.GROUPS.name();
            actionForm.set("organizedBy", organizedBy);
        }

        computeCurricularInfo(request, registration);

        if (StringUtils.isEmpty(request.getParameter("degreeCurricularPlanID"))) {
            return mapping.findForward("ShowStudentCurriculum");
        } else {
            request.setAttribute("degreeCurricularPlanID", request.getParameter("degreeCurricularPlanID"));
            return mapping.findForward("ShowStudentCurriculumForCoordinator");
        }
    }

    private List<StudentCurricularPlan> getSelectedStudentCurricularPlans(final Registration registration,
            final String studentCPID) {
        final List<StudentCurricularPlan> result;

        final StudentCurricularPlanIDDomainType scpIdType = new StudentCurricularPlanIDDomainType(studentCPID);
        if (scpIdType.isNewest()) {
            result = Collections.singletonList(registration.getLastStudentCurricularPlan());
        } else if (scpIdType.isAll()) {
            result = getSortedStudentCurricularPlans(registration);
        } else {
            result = Collections.singletonList(getStudentCurricularPlan(registration, studentCPID));
        }

        return result;
    }

    private StudentCurricularPlanIDDomainType getDefaultStudentCPID(final Registration registration) {
        return StudentCurricularPlanIDDomainType.NEWEST;
    }

    private String getStudentCPID(HttpServletRequest request, DynaActionForm actionForm) {
        String result = request.getParameter("studentCPID");
        if (result == null) {
            result = (String) request.getAttribute("studentCPID");
        }
        if (result == null) {
            result = (String) actionForm.get("studentCPID");
        }

        return result;
    }

    private static List<StudentCurricularPlan> getSortedStudentCurricularPlans(final Registration registration) {
        final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        result.addAll(registration.getStudentCurricularPlansSet());
        Collections.sort(result, new BeanComparator("startDateYearMonthDay"));

        return result;
    }

    private StudentCurricularPlan getStudentCurricularPlan(final Registration registration, final String scpId) {
        for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getExternalId().equals(scpId)) {
                return studentCurricularPlan;
            }
        }

        return null;
    }

    private List<LabelValueBean> getSCPsLabelValueBeanList(Collection<StudentCurricularPlan> studentCurricularPlans) {
        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.NEWEST_STRING,
                StudentCurricularPlanIDDomainType.NEWEST.toString()));
        result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.ALL_STRING,
                StudentCurricularPlanIDDomainType.ALL.toString()));

        for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
            final StringBuilder label = new StringBuilder();

            label.append(studentCurricularPlan.getRegistration().getDegreeNameWithDescription());
            label.append(", ").append(studentCurricularPlan.getDegreeCurricularPlan().getName());

            label.append(" - ").append(studentCurricularPlan.getStartDateYearMonthDay());

            result.add(new LabelValueBean(label.toString(), String.valueOf(studentCurricularPlan.getExternalId())));
        }

        return result;
    }

    public ActionForward getCurriculumForCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ExistingActionException, FenixServiceException {

        // get and set the degreeCurricularPlanID from the request and onto the
        // request
        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        final String studentCurricularPlanID = getStudentCPID(request, (DynaActionForm) form);

        String executionDegreeID = getExecutionDegree(request);
        List result = null;
        try {
            // TODO check
            result = ReadStudentCurriculum.runReadStudentCurriculum(executionDegreeID, studentCurricularPlanID);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        }
        // TODO Remove this exception! It returns null and it is not supposed!
        catch (Exception exp) {
            logger.error(exp.getMessage(), exp);
            return null;
        }

        BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
        BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(courseName);
        chainComparator.addComparator(executionYear);

        Collections.sort(result, chainComparator);

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try {

            infoStudentCurricularPlan = ReadStudentCurricularPlan.run(studentCurricularPlanID);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }

        request.setAttribute(PresentationConstants.CURRICULUM, result);
        request.setAttribute(PresentationConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

        return mapping.findForward("ShowStudentCurriculum");
    }

    private String getExecutionDegree(HttpServletRequest request) {
        String executionDegreeIdString = request.getParameter("executionDegreeId");
        if (executionDegreeIdString == null) {
            executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
        }
        request.setAttribute("executionDegreeId", executionDegreeIdString);

        return executionDegreeIdString;
    }

    public static JsonObject computeCurricularInfo(HttpServletRequest request, Registration registration) {

        /* Make a 'studentStatistics' Array of ExecutionPeriodStatisticsBean that has info on # enrolments, etc */
        List<ExecutionPeriodStatisticsBean> studentStatistics = new ArrayList<ExecutionPeriodStatisticsBean>();

        Map<ExecutionInterval, ExecutionPeriodStatisticsBean> enrolmentsByExecutionPeriod = new HashMap<>();

        for (StudentCurricularPlan studentCurricularPlan : getSortedStudentCurricularPlans(registration)) {
            for (ExecutionInterval executionInterval : studentCurricularPlan.getEnrolmentsExecutionPeriods()) {
                if (enrolmentsByExecutionPeriod.containsKey(executionInterval)) {
                    ExecutionPeriodStatisticsBean executionPeriodStatisticsBean =
                            enrolmentsByExecutionPeriod.get(executionInterval);
                    executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(
                            studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionInterval));
                    enrolmentsByExecutionPeriod.put(executionInterval, executionPeriodStatisticsBean);
                } else {
                    ExecutionPeriodStatisticsBean executionPeriodStatisticsBean =
                            new ExecutionPeriodStatisticsBean(executionInterval);
                    executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(
                            studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionInterval));
                    enrolmentsByExecutionPeriod.put(executionInterval, executionPeriodStatisticsBean);
                }
            }
        }
        studentStatistics.addAll(enrolmentsByExecutionPeriod.values());
        Collections.sort(studentStatistics, new BeanComparator("executionPeriod"));

        /* Put all the info in the required JSON format */
        JsonObject curricularInfoJSONObject = new JsonObject();

        JsonArray periodsJSONArray = new JsonArray();
        for (ExecutionPeriodStatisticsBean executionPeriodStatisticsBean : studentStatistics) {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(new JsonPrimitive(executionPeriodStatisticsBean.getExecutionPeriod().getExecutionYear().getYear()
                    + " - " + executionPeriodStatisticsBean.getExecutionPeriod().getChildOrder().toString() + "º sem"));
            jsonArray.add(new JsonPrimitive(executionPeriodStatisticsBean.getTotalEnrolmentsNumber()));
            jsonArray.add(new JsonPrimitive(executionPeriodStatisticsBean.getApprovedEnrolmentsNumber()));
            periodsJSONArray.add(jsonArray);
        }
        curricularInfoJSONObject.add("periods", periodsJSONArray);

        /* Serve the JSON object */
        request.setAttribute("registrationApprovalRateJSON", curricularInfoJSONObject);

        return curricularInfoJSONObject;
    }
}
