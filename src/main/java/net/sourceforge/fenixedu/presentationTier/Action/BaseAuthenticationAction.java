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
package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.AuthorizationException;

import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public abstract class BaseAuthenticationAction extends FenixAction {

    @Override
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {

            final User userView = doAuthentication(form, request);

            if (userView == null || userView.getPerson().getPersonRolesSet().isEmpty()) {
                return getAuthenticationFailedForward(mapping, request, "errors.noAuthorization", "errors.noAuthorization");
            }

            final HttpSession httpSession = request.getSession(false);

            if (hasMissingTeacherService(userView)) {
                return handleSessionCreationAndForwardToTeachingService(request, userView, httpSession);
            } else if (hasPendingTeachingReductionService(userView)) {
                return handleSessionCreationAndForwardToPendingTeachingReductionService(request, userView, httpSession);
            } else if (hasMissingRAIDESInformation(userView)) {
                return handleSessionCreationAndForwardToRAIDESInquiriesResponseQuestion(request, userView, httpSession);
            } else if (isAlumniAndHasInquiriesToResponde(userView)) {
                return handleSessionCreationAndForwardToAlumniInquiriesResponseQuestion(request, userView, httpSession);
            } else if (isStudentAndHasQucInquiriesToRespond(userView)) {
                return handleSessionCreationAndForwardToQucInquiriesResponseQuestion(request, userView, httpSession);
            } else if (isDelegateAndHasInquiriesToRespond(userView)) {
                return handleSessionCreationAndForwardToDelegateInquiriesResponseQuestion(request, userView, httpSession);
            } else if (isTeacherAndHasInquiriesToRespond(userView)) {
                return handleSessionCreationAndForwardToTeachingInquiriesResponseQuestion(request, userView, httpSession);
            } else if (isRegentAndHasInquiriesToRespond(userView)) {
                return handleSessionCreationAndForwardToRegentInquiriesResponseQuestion(request, userView, httpSession);
            } else if (isCoordinatorAndHasReportsToRespond(userView)) {
                return handleSessionCreationAndForwardToCoordinationExecutionDegreeReportsQuestion(request, userView, httpSession);
            } else if (isStudentAndHasFirstTimeCycleInquiryToRespond(userView)) {
                return handleSessionCreationAndForwardToFirstTimeCycleInquiry(request, userView, httpSession);
            } else if (isStudentAndHasGratuityDebtsToPay(userView)) {
                return handleSessionCreationAndForwardToGratuityPaymentsReminder(request, userView, httpSession);
            } else if (isAlumniWithNoData(userView)) {
                return handleSessionCreationAndForwardToAlumniReminder(request, userView, httpSession);
            } else if (hasPendingPartyContactValidationRequests(userView)) {
                return handlePartyContactValidationRequests(request, userView, httpSession);
            } else {
                return handleSessionCreationAndGetForward(mapping, request, userView, httpSession);
            }
        } catch (AuthorizationException e) {
            return getAuthenticationFailedForward(mapping, request, "invalidAuthentication", "errors.invalidAuthentication");
        }
    }

    private ActionForward handleSessionCreationAndForwardToFirstTimeCycleInquiry(HttpServletRequest request, User userView,
            HttpSession session) {
        return new ActionForward("/respondToFirstTimeCycleInquiry.do?method=showQuestion");
    }

    private boolean isStudentAndHasFirstTimeCycleInquiryToRespond(User userView) {
        if (userView.getPerson().hasRole(RoleType.STUDENT)) {
            final Student student = userView.getPerson().getStudent();
            return student != null && student.hasFirstTimeCycleInquiryToRespond();
        }
        return false;
    }

    private boolean hasMissingTeacherService(User userView) {
        if (userView.getPerson() != null && userView.getPerson().getTeacher() != null
                && userView.getPerson().hasRole(RoleType.DEPARTMENT_MEMBER)) {
            ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
            if (executionSemester != null
                    && (userView.getPerson().getTeacher().isActiveForSemester(executionSemester) || userView.getPerson()
                            .getTeacher().getTeacherAuthorization(executionSemester) != null)) {
                TeacherService teacherService =
                        userView.getPerson().getTeacher().getTeacherServiceByExecutionPeriod(executionSemester);
                return (teacherService == null || teacherService.getTeacherServiceLock() == null)
                        && executionSemester.isInValidCreditsPeriod(RoleType.DEPARTMENT_MEMBER);
            }
        }
        return false;
    }

    private boolean hasPendingTeachingReductionService(User userView) {
        if (userView.getPerson() != null && userView.getPerson().getTeacher() != null
                && userView.getPerson().hasRole(RoleType.DEPARTMENT_MEMBER)) {
            Department department = userView.getPerson().getTeacher().getCurrentWorkingDepartment();
            if (department != null && department.isCurrentUserCurrentDepartmentPresident()) {
                ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
                if (executionSemester != null
                        && executionSemester.isInValidCreditsPeriod(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                    boolean inValidTeacherCreditsPeriod = executionSemester.isInValidCreditsPeriod(RoleType.DEPARTMENT_MEMBER);
                    for (ReductionService reductionService : department.getPendingReductionServicesSet()) {
                        if ((reductionService.getTeacherService().getTeacherServiceLock() != null || !inValidTeacherCreditsPeriod)
                                && reductionService.getTeacherService().getExecutionPeriod().equals(executionSemester)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ActionForward handlePartyContactValidationRequests(HttpServletRequest request, User userView, HttpSession session) {
        return new ActionForward("/partyContactValidationReminder.do?method=showReminder");
    }

    private boolean hasMissingRAIDESInformation(User userView) {
        return userView.getPerson() != null && userView.getPerson().hasStudent()
                && userView.getPerson().getStudent().hasAnyMissingPersonalInformation();
    }

    private boolean hasPendingPartyContactValidationRequests(User userView) {
        final Person person = userView.getPerson();
        return person.hasPendingPartyContacts() && person.getCanValidateContacts();
    }

    private boolean isAlumniAndHasInquiriesToResponde(final User userView) {
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : userView.getPerson().getCerimonyInquiryPersonSet()) {
            if (cerimonyInquiryPerson.isPendingResponse()) {
                return true;
            }
        }
        return false;
    }

    private ActionForward handleSessionCreationAndForwardToAlumniReminder(HttpServletRequest request, User userView,
            HttpSession session) {
        return new ActionForward("/alumniReminder.do");
    }

    /**
     * Checks if all the person that have the Alumni object have the any
     * formation filled in with the exception for those that are active teachers
     * or haver a role of EMPLOYEE or RESEARCHER
     * 
     * @param userView
     * @return true if it has alumni and the formations list is not empty, false
     *         otherwise and if it falls under the specific cases described
     *         above
     */
    private boolean isAlumniWithNoData(User userView) {
        Person person = userView.getPerson();
        if (person.getStudent() != null && person.getStudent().getAlumni() != null) {
            if ((person.getTeacher() != null && person.getTeacher().isActive())
                    || person.getPersonRole(RoleType.EMPLOYEE) != null || person.getPersonRole(RoleType.RESEARCHER) != null) {
                return false;
            }
            return person.getFormations().isEmpty();
        }
        return false;
    }

    private ActionForward handleSessionCreationAndForwardToGratuityPaymentsReminder(HttpServletRequest request, User userView,
            HttpSession session) {
        return new ActionForward("/gratuityPaymentsReminder.do?method=showReminder");
    }

    private boolean isStudentAndHasGratuityDebtsToPay(final User userView) {
        return userView.getPerson().hasRole(RoleType.STUDENT)
                && userView.getPerson().hasGratuityOrAdministrativeOfficeFeeAndInsuranceDebtsFor(
                        ExecutionYear.readCurrentExecutionYear());
    }

    private boolean isTeacherAndHasInquiriesToRespond(User userView) {
        if (userView.getPerson().hasRole(RoleType.TEACHER)
                || (TeacherInquiryTemplate.getCurrentTemplate() != null && !userView.getPerson()
                        .getProfessorships(TeacherInquiryTemplate.getCurrentTemplate().getExecutionPeriod()).isEmpty())) {
            return userView.getPerson().hasTeachingInquiriesToAnswer();
        }
        return false;
    }

    private boolean isRegentAndHasInquiriesToRespond(User userView) {
        if (userView.getPerson().hasRole(RoleType.TEACHER)
                || (RegentInquiryTemplate.getCurrentTemplate() != null && !userView.getPerson()
                        .getProfessorships(RegentInquiryTemplate.getCurrentTemplate().getExecutionPeriod()).isEmpty())) {
            return userView.getPerson().hasRegentInquiriesToAnswer();
        }
        return false;
    }

    private boolean isCoordinatorAndHasReportsToRespond(User userView) {
        if (userView.getPerson().hasRole(RoleType.COORDINATOR)) {
            return userView.getPerson().hasCoordinationExecutionDegreeReportsToAnswer();
        }
        return false;
    }

    private boolean isStudentAndHasQucInquiriesToRespond(final User userView) {
        if (userView.getPerson().hasRole(RoleType.STUDENT)) {
            final Student student = userView.getPerson().getStudent();
            return student != null && student.hasInquiriesToRespond();
        }
        return false;
    }

    private boolean isDelegateAndHasInquiriesToRespond(final User userView) {
        if (userView.getPerson().hasRole(RoleType.DELEGATE)) {
            final Student student = userView.getPerson().getStudent();
            return student != null && student.hasYearDelegateInquiriesToAnswer();
        }
        return false;
    }

    protected abstract User doAuthentication(ActionForm form, HttpServletRequest request);

    protected ActionForward getAuthenticationFailedForward(final ActionMapping mapping, final HttpServletRequest request,
            final String actionKey, final String messageKey) {
        final ActionMessages actionErrors = new ActionMessages();
        actionErrors.add(actionKey, new ActionMessage(messageKey));
        saveErrors(request, actionErrors);
        return new ActionForward("/loginPage.jsp");
    }

    private ActionForward handleSessionCreationAndGetForward(ActionMapping mapping, HttpServletRequest request, User userView,
            final HttpSession session) {
        return new ActionForward("/home.do", true);
    }

    private ActionForward handleSessionCreationAndForwardToTeachingService(HttpServletRequest request, User userView,
            HttpSession session) {
        String teacherOid = userView.getPerson().getTeacher().getExternalId();
        String executionYearOid = ExecutionYear.readCurrentExecutionYear().getExternalId();

        HtmlLink link = new HtmlLink();
        link.setModule("/departmentMember");
        link.setUrl("/credits.do?method=viewAnnualTeachingCredits&teacherOid=" + teacherOid + "&executionYearOid="
                + executionYearOid);
        link.setEscapeAmpersand(false);
        String calculatedUrl = link.calculateUrl();
        return new ActionForward("/departmentMember/credits.do?method=viewAnnualTeachingCredits&teacherOid=" + teacherOid
                + "&executionYearOid=" + executionYearOid + "&_request_checksum_="
                + GenericChecksumRewriter.calculateChecksum(calculatedUrl, session), true);
    }

    private ActionForward handleSessionCreationAndForwardToPendingTeachingReductionService(HttpServletRequest request,
            User userView, HttpSession session) {
        HtmlLink link = new HtmlLink();
        link.setModule("/departmentMember");
        link.setUrl("/creditsReductions.do?method=showReductionServices");
        link.setEscapeAmpersand(false);
        String calculatedUrl = link.calculateUrl();
        return new ActionForward("/departmentMember/creditsReductions.do?method=showReductionServices&_request_checksum_="
                + GenericChecksumRewriter.calculateChecksum(calculatedUrl, session), true);
    }

    private ActionForward handleSessionCreationAndForwardToRAIDESInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        HtmlLink link = new HtmlLink();
        link.setModule("/student");
        link.setUrl("/editMissingCandidacyInformation.do?method=prepareEdit");
        link.setEscapeAmpersand(false);
        String calculatedUrl = link.calculateUrl();
        return new ActionForward("/student/editMissingCandidacyInformation.do?method=prepareEdit&_request_checksum_="
                + GenericChecksumRewriter.calculateChecksum(calculatedUrl, session), true);
    }

    private ActionForward handleSessionCreationAndForwardToAlumniInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToAlumniInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToQucInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToDelegateInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToYearDelegateInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToTeachingInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToTeachingInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToRegentInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToRegentInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToCoordinationExecutionDegreeReportsQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToCoordinationExecutionDegreeReportsQuestion.do?method=showQuestion");
    }

}