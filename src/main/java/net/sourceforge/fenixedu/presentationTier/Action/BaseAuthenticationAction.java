package net.sourceforge.fenixedu.presentationTier.Action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.domain.PendingRequestParameter;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.LoginRedirectAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.AuthorizationException;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.FenixFramework;

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

            String pendingRequest = request.getParameter("pendingRequest");
            if (pendingRequest != null && pendingRequest.length() > 0 && !pendingRequest.equals("null")
                    && FenixFramework.getDomainObject(pendingRequest) != null
                    && isValidChecksumForUser((PendingRequest) FenixFramework.getDomainObject(pendingRequest))) {
                return handleSessionRestoreAndGetForward(request, form, userView, httpSession);
            } else if (hasMissingTeacherService(userView)) {
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
        final List<Content> contents = new ArrayList<Content>();
        RootDomainObject.getInstance().getRootPortal().addPathContentsForTrailingPath(contents, "departamento/departamento");
        final FilterFunctionalityContext context = new FilterFunctionalityContext(request, contents);
        request.setAttribute(FilterFunctionalityContext.CONTEXT_KEY, context);

        String teacherOid = userView.getPerson().getTeacher().getExternalId();
        String executionYearOid = ExecutionYear.readCurrentExecutionYear().getExternalId();

        HtmlLink link = new HtmlLink();
        link.setModule("/departmentMember");
        link.setUrl("/credits.do?method=viewAnnualTeachingCredits&teacherOid=" + teacherOid + "&executionYearOid="
                + executionYearOid + "&"
                + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                + "=/departamento/departamento");
        link.setEscapeAmpersand(false);
        String calculatedUrl = link.calculateUrl();
        return new ActionForward(
                "/departmentMember/credits.do?method=viewAnnualTeachingCredits&teacherOid="
                        + teacherOid
                        + "&executionYearOid="
                        + executionYearOid
                        + "&"
                        + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                        + "=/departamento/departamento&_request_checksum_="
                        + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter
                                .calculateChecksum(calculatedUrl),
                true);
    }

    private ActionForward handleSessionCreationAndForwardToPendingTeachingReductionService(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        final List<Content> contents = new ArrayList<Content>();
        RootDomainObject.getInstance().getRootPortal().addPathContentsForTrailingPath(contents, "departamento/departamento");
        final FilterFunctionalityContext context = new FilterFunctionalityContext(request, contents);
        request.setAttribute(FilterFunctionalityContext.CONTEXT_KEY, context);

        String teacherOid = userView.getPerson().getTeacher().getExternalId();
        String executionYearOid = ExecutionYear.readCurrentExecutionYear().getExternalId();

        HtmlLink link = new HtmlLink();
        link.setModule("/departmentMember");
        link.setUrl("/creditsReductions.do?method=showReductionServices&"
                + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                + "=/departamento/departamento");
        link.setEscapeAmpersand(false);
        String calculatedUrl = link.calculateUrl();
        return new ActionForward(
                "/departmentMember/creditsReductions.do?method=showReductionServices&"
                        + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                        + "=/departamento/departamento&_request_checksum_="
                        + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter
                                .calculateChecksum(calculatedUrl),
                true);
    }

    private ActionForward handleSessionCreationAndForwardToRAIDESInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        final List<Content> contents = new ArrayList<Content>();
        RootDomainObject.getInstance().getRootPortal().addPathContentsForTrailingPath(contents, "estudante/estudante");
        final FilterFunctionalityContext context = new FilterFunctionalityContext(request, contents);
        request.setAttribute(FilterFunctionalityContext.CONTEXT_KEY, context);

        HtmlLink link = new HtmlLink();
        link.setModule("/student");
        link.setUrl("/editMissingCandidacyInformation.do?method=prepareEdit&"
                + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                + "=/estudante/estudante");
        link.setEscapeAmpersand(false);
        String calculatedUrl = link.calculateUrl();
        return new ActionForward(
                "/student/editMissingCandidacyInformation.do?method=prepareEdit&"
                        + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                        + "=/estudante/estudante&_request_checksum_="
                        + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter
                                .calculateChecksum(calculatedUrl),
                true);
    }

    private ActionForward handleSessionCreationAndForwardToAlumniInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToAlumniInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToTeacherInquiriesResponseQuestion(HttpServletRequest request,
            User userView, HttpSession session) {
        return new ActionForward("/respondToInquiriesQuestion.do?method=showTeacherQuestion");
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

    private boolean isValidChecksumForUser(final PendingRequest pendingRequest) {
        try {
            String url = pendingRequest.getUrl();

            for (PendingRequestParameter pendingRequestParameter : pendingRequest.getPendingRequestParameter()) {
                if (!pendingRequestParameter.getAttribute()) {
                    url =
                            LoginRedirectAction.addToUrl(url, pendingRequestParameter.getParameterKey(),
                                    pendingRequestParameter.getParameterValue());
                }
            }

            final String requestChecksumParameter = pendingRequest.getRequestChecksumParameter();
            
            if (url.contains("/external/") && requestChecksumParameter == null) { //TODO: check if it is necessary
            	return true;
            }
            return GenericChecksumRewriter.calculateChecksum(url).equals(requestChecksumParameter);
        } catch (Exception ex) {
            return false;
        }
    }

    private ActionForward handleSessionRestoreAndGetForward(HttpServletRequest request, ActionForm form, User userView,
            final HttpSession session) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setContextRelative(false);
        actionForward.setRedirect(true);
        // Set request attributes

        String pendingRequest = request.getParameter("pendingRequest");
        if (pendingRequest == null) {
            pendingRequest = (String) request.getAttribute("pendingRequest");
            if (pendingRequest == null) {
                final DynaActionForm authenticationForm = (DynaActionForm) form;
                pendingRequest = (String) authenticationForm.get("pendingRequest");
            }
        }
        actionForward.setPath("/redirect.do?pendingRequest=" + pendingRequest);
        return actionForward;
    }

    /**
     * @param userRoles
     * @return
     */
    private int getNumberOfSubApplications(final Collection<RoleType> roleTypes) {
        final Set<String> subApplications = new HashSet<String>();
        for (final RoleType roleType : roleTypes) {
            final Role role = Role.getRoleByRoleType(roleType);
            final String subApplication = role.getPortalSubApplication();
            if (!subApplications.contains(subApplication) && !subApplication.equals("/teacher")) {
                subApplications.add(subApplication);
            }
        }
        return subApplications.size();
    }

    /**
     * @param infoRole
     * @return
     */
    private ActionForward buildRoleForward(Role infoRole) {
        ActionForward actionForward = new ActionForward();
        actionForward.setContextRelative(false);
        actionForward.setRedirect(false);
        actionForward.setPath("/dotIstPortal.do?prefix=" + infoRole.getPortalSubApplication() + "&page=" + infoRole.getPage());
        return actionForward;
    }

    public static String getRemoteHostName(HttpServletRequest request) {
        String remoteHostName;
        final String remoteAddress = HostAccessControl.getRemoteAddress(request);
        try {
            remoteHostName = InetAddress.getByName(remoteAddress).getHostName();
        } catch (UnknownHostException e) {
            remoteHostName = remoteAddress;
        }
        return remoteHostName;
    }
}