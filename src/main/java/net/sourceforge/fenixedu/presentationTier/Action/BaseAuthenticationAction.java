package net.sourceforge.fenixedu.presentationTier.Action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.LoginRedirectAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTime;
import org.joda.time.Days;

import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.fenixframework.pstm.VersionNotAvailableException;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public abstract class BaseAuthenticationAction extends FenixAction {

    @Override
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {

            String remoteHostName = getRemoteHostName(request);
            final IUserView userView = doAuthentication(form, request, remoteHostName);

            if (userView == null || userView.getRoleTypes().isEmpty()) {
                return getAuthenticationFailedForward(mapping, request, "errors.noAuthorization", "errors.noAuthorization");
            }

            final HttpSession httpSession = request.getSession(false);

            UserView.setUser(userView);
            String pendingRequest = request.getParameter("pendingRequest");
            if (pendingRequest != null && pendingRequest.length() > 0 && !pendingRequest.equals("null")
                    && AbstractDomainObject.fromExternalId(pendingRequest) != null
                    && isValidChecksumForUser((PendingRequest) AbstractDomainObject.fromExternalId(pendingRequest))) {
                return handleSessionRestoreAndGetForward(request, form, userView, httpSession);
            } else if (hasMissingTeacherService(userView)) {
                return handleSessionCreationAndForwardToTeachingService(request, userView, httpSession);
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
        } catch (ExcepcaoAutenticacao e) {
            return getAuthenticationFailedForward(mapping, request, "invalidAuthentication", "errors.invalidAuthentication");
        }
    }

    private ActionForward handleSessionCreationAndForwardToFirstTimeCycleInquiry(HttpServletRequest request, IUserView userView,
            HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToFirstTimeCycleInquiry.do?method=showQuestion");
    }

    private boolean isStudentAndHasFirstTimeCycleInquiryToRespond(IUserView userView) {
        if (userView.hasRoleType(RoleType.STUDENT)) {
            final Student student = userView.getPerson().getStudent();
            return student != null && student.hasFirstTimeCycleInquiryToRespond();
        }
        return false;
    }

    private boolean hasMissingTeacherService(IUserView userView) {
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

    private ActionForward handlePartyContactValidationRequests(HttpServletRequest request, IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/partyContactValidationReminder.do?method=showReminder");
    }

    private boolean hasMissingRAIDESInformation(IUserView userView) {
        return userView.getPerson() != null && userView.getPerson().hasStudent()
                && userView.getPerson().getStudent().hasAnyMissingPersonalInformation();
    }

    private boolean hasPendingPartyContactValidationRequests(IUserView userView) {
        final Person person = userView.getPerson();
        return person.hasPendingPartyContacts() && person.getCanValidateContacts();
    }

    private boolean isAlumniAndHasInquiriesToResponde(final IUserView userView) {
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : userView.getPerson().getCerimonyInquiryPersonSet()) {
            if (cerimonyInquiryPerson.isPendingResponse()) {
                return true;
            }
        }
        return false;
    }

    private ActionForward handleSessionCreationAndForwardToAlumniReminder(HttpServletRequest request, IUserView userView,
            HttpSession session) {
        createNewSession(request, session, userView);
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
    private boolean isAlumniWithNoData(IUserView userView) {
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

    private ActionForward handleSessionCreationAndForwardToGratuityPaymentsReminder(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/gratuityPaymentsReminder.do?method=showReminder");
    }

    private boolean isStudentAndHasGratuityDebtsToPay(final IUserView userView) {
        return userView.hasRoleType(RoleType.STUDENT)
                && userView.getPerson().hasGratuityOrAdministrativeOfficeFeeAndInsuranceDebtsFor(
                        ExecutionYear.readCurrentExecutionYear());
    }

    private boolean isTeacherAndHasInquiriesToRespond(IUserView userView) {
        if (userView.hasRoleType(RoleType.TEACHER)
                || (TeacherInquiryTemplate.getCurrentTemplate() != null && !userView.getPerson()
                        .getProfessorships(TeacherInquiryTemplate.getCurrentTemplate().getExecutionPeriod()).isEmpty())) {
            return userView.getPerson().hasTeachingInquiriesToAnswer();
        }
        return false;
    }

    private boolean isRegentAndHasInquiriesToRespond(IUserView userView) {
        if (userView.hasRoleType(RoleType.TEACHER)
                || (RegentInquiryTemplate.getCurrentTemplate() != null && !userView.getPerson()
                        .getProfessorships(RegentInquiryTemplate.getCurrentTemplate().getExecutionPeriod()).isEmpty())) {
            return userView.getPerson().hasRegentInquiriesToAnswer();
        }
        return false;
    }

    private boolean isCoordinatorAndHasReportsToRespond(IUserView userView) {
        if (userView.hasRoleType(RoleType.COORDINATOR)) {
            return userView.getPerson().hasCoordinationExecutionDegreeReportsToAnswer();
        }
        return false;
    }

    private boolean isStudentAndHasQucInquiriesToRespond(final IUserView userView) {
        if (userView.hasRoleType(RoleType.STUDENT)) {
            final Student student = userView.getPerson().getStudent();
            return student != null && student.hasInquiriesToRespond();
        }
        return false;
    }

    private boolean isDelegateAndHasInquiriesToRespond(final IUserView userView) {
        if (userView.hasRoleType(RoleType.DELEGATE)) {
            final Student student = userView.getPerson().getStudent();
            return student != null && student.hasYearDelegateInquiriesToAnswer();
        }
        return false;
    }

    protected abstract IUserView doAuthentication(ActionForm form, HttpServletRequest request, String remoteHostName)
            throws  FenixServiceException;

    protected abstract ActionForward getAuthenticationFailedForward(final ActionMapping mapping,
            final HttpServletRequest request, final String actionKey, final String messageKey);

    private ActionForward handleSessionCreationAndGetForward(ActionMapping mapping, HttpServletRequest request,
            IUserView userView, final HttpSession session) {
        createNewSession(request, session, userView);

        ActionForward actionForward = mapping.findForward("sucess");

        return checkExpirationDate(mapping, request, userView, actionForward);
    }

    private ActionForward handleSessionCreationAndForwardToTeachingService(HttpServletRequest request, IUserView userView,
            HttpSession session) {
        createNewSession(request, session, userView);
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

    private ActionForward handleSessionCreationAndForwardToRAIDESInquiriesResponseQuestion(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);

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
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToAlumniInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToTeacherInquiriesResponseQuestion(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToInquiriesQuestion.do?method=showTeacherQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToQucInquiriesResponseQuestion(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToDelegateInquiriesResponseQuestion(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToYearDelegateInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToTeachingInquiriesResponseQuestion(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToTeachingInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToRegentInquiriesResponseQuestion(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToRegentInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToCoordinationExecutionDegreeReportsQuestion(HttpServletRequest request,
            IUserView userView, HttpSession session) {
        createNewSession(request, session, userView);
        return new ActionForward("/respondToCoordinationExecutionDegreeReportsQuestion.do?method=showQuestion");
    }

    private ActionForward checkExpirationDate(ActionMapping mapping, HttpServletRequest request, IUserView userView,
            ActionForward actionForward) {
        if (userView.getExpirationDate() == null) {
            return actionForward;
        }

        Days days = Days.daysBetween(new DateTime(), userView.getExpirationDate());
        if (days.getDays() <= 30) {
            request.setAttribute("path", actionForward.getPath());
            request.setAttribute("days", days.getDays());
            request.setAttribute("dayString", userView.getExpirationDate().toString("dd/MM/yyyy"));
            return mapping.findForward("expirationWarning");
        } else {
            return actionForward;
        }
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
            return GenericChecksumRewriter.calculateChecksum(url).equals(requestChecksumParameter);
        } catch (VersionNotAvailableException ex) {
            return false;
        }
    }

    private ActionForward handleSessionRestoreAndGetForward(HttpServletRequest request, ActionForm form, IUserView userView,
            final HttpSession session) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setContextRelative(false);
        actionForward.setRedirect(true);
        createNewSession(request, session, userView);
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

    private HttpSession createNewSession(final HttpServletRequest request, final HttpSession session, final IUserView userView) {
        final Locale locale = Language.getLocale();
        if (session != null) {
            try {
                session.invalidate();
            } catch (final IllegalStateException ise) {
                // session already invalidated... that's ok just create a new
                // one and proceed happily fipping and flopping.
            }
        }

        final HttpSession newSession = request.getSession(true);

        // Store the UserView into the session and return
        UserView.setUser(userView);
        newSession.setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, userView);

        if (locale == null) {
            I18NFilter.setDefaultLocale(request, newSession);
        } else {
            I18NFilter.setLocale(request, newSession, locale);
        }

        return newSession;
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