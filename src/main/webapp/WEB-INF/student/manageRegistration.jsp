<%@ page import="org.fenixedu.academic.domain.Degree" %>
<%@ page import="org.fenixedu.academic.domain.accounting.EventTemplate" %>
<%@ page import="org.fenixedu.academic.domain.accounting.EventTemplateConfig" %>
<%@ page import="org.fenixedu.academic.domain.student.Registration" %>
<%@ page import="org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear" %>
<%@ page import="org.fenixedu.academic.domain.student.Student" %>
<%@ page import="org.fenixedu.bennu.core.security.Authenticate" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<h2>
    <spring:message code="title.manage.registration" text="Manage Registrations"/>
</h2>

<%
    final Student student = Authenticate.getUser().getPerson().getStudent();
    final Set<Registration> registrations = new HashSet();
    for (final Registration registration : student.getRegistrationsSet()) {
        final EventTemplate eventTemplate = registration.getEventTemplate();
        if (registration.isActive() && eventTemplate != null && !eventTemplate.getAlternativeEventTemplateSet().isEmpty()) {
            registrations.add(registration);
        }
    }
%>
<%
    for (final Registration registration : student.getRegistrationsSet()) {
        for (final RegistrationDataByExecutionYear dataByYear : registration.getRegistrationDataByExecutionYearSet()) {
            if (dataByYear.getExecutionYear().isCurrent()) {
                final Degree degree = registration.getDegree();
                final EventTemplate eventTemplate = EventTemplate.templateFor(dataByYear);
                final EventTemplateConfig templateConfig = eventTemplate == null ? null
                        : eventTemplate.getConfigFor(new DateTime());
%>
                <h3>
                    <%= degree.getPresentationNameI18N().getContent() %>
                </h3>
                <ul>
                    <li>
                        <spring:message code="label.executionYear" text="Year"/>:
                        <strong>
                            <%= dataByYear.getExecutionYear().getName() %>
                        </strong>
                    </li>
                    <li>
                        <spring:message code="label.registrationDate" text="Registration Date"/>:
                        <%= dataByYear.getEnrolmentDate().toString("yyyy-MM-dd") %>
                    </li>
                    <li>
                        <spring:message code="label.enrolment.limit" text="Enrolment Limit"/>:
                        <strong>
                            <%= dataByYear.getMaxCreditsPerYear() == null ? "100%"
                                    : (dataByYear.getMaxCreditsPerYear().toString() + " ECTS") %>
                        </strong>
                    </li>
                    <li>
                        <spring:message code="label.allowed.semesters.for.enrolment" text="Allowed Semesters for Enrolment"/>:
                        <strong>
                            <% if (dataByYear.getAllowedSemesterForEnrolments() == null) { %>
                                <spring:message code="label.both.semesters" text="Both"/>
                            <% } else { %>
                                <%= dataByYear.getAllowedSemesterForEnrolments().getQualifiedName() %>
                            <% } %>
                        </strong>
                    </li>
                    <li>
                        <spring:message code="label.payment.plan" text="Payment Plan"/>:
                        <% if (eventTemplate == null) { %>
                            <spring:message code="label.pending" text="Pending"/>
                        <% } else { %>
                            <%= eventTemplate.getTitle().getContent() %>
                        <% } %>
                        <ul>
                            <li>
                                <spring:message code="label.tuition.value" text="Tuition"/>:
                                <% if (templateConfig == null) { %>
                                <spring:message code="label.pending" text="Pending"/>
                                <% } else { %>
                                <%= templateConfig.valueFor(EventTemplate.Type.TUITION) %>
                                <spring:message code="label.euro" text="Euro"/>:
                                <% } %>
                            </li>
                            <li>
                                <spring:message code="label.insurance.value" text="Insurance"/>:
                                <% if (templateConfig == null) { %>
                                <spring:message code="label.pending" text="Pending"/>
                                <% } else { %>
                                <%= templateConfig.valueFor(EventTemplate.Type.INSURANCE) %>
                                <spring:message code="label.euro" text="Euro"/>:
                                <% } %>
                            </li>
                            <li>
                                <spring:message code="label.adminFees.value" text="Administrative Fees"/>:
                                <% if (templateConfig == null) { %>
                                <spring:message code="label.pending" text="Pending"/>
                                <% } else { %>
                                <%= templateConfig.valueFor(EventTemplate.Type.ADMIN_FEES) %>
                                <spring:message code="label.euro" text="Euro"/>:
                                <% } %>
                            </li>
                        </ul>
                    </li>
                </ul>

                <% if (eventTemplate != null && eventTemplate.getAlternativeEventTemplateSet().size() > 0) { %>
                    <a href="/registrationSelfService/<%= dataByYear.getExternalId() %>/changePlan" class="btn btn-default">
                        <spring:message code="label.change.plan" text="Change Plan"/>
                    </a>
                <% } %>
<%
            }
        }
    }
%>
