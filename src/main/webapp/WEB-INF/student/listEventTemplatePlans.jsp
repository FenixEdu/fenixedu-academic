<%@ page import="org.fenixedu.academic.domain.Degree" %>
<%@ page import="org.fenixedu.academic.domain.accounting.EventTemplate" %>
<%@ page import="org.fenixedu.academic.domain.student.Registration" %>
<%@ page import="org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear" %>
<%@ page import="java.util.TreeSet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<h2>
    <spring:message code="title.manage.registration" text="Manage Registrations"/>
</h2>

<%
    final RegistrationDataByExecutionYear dataByYear = (RegistrationDataByExecutionYear) request.getAttribute("dataByExecutionYear");
    final Registration registration = dataByYear.getRegistration();
    final Degree degree = registration.getDegree();
    final EventTemplate eventTemplate = EventTemplate.templateFor(dataByYear);
%>

<h3>
    <%= degree.getPresentationNameI18N().getContent() %>
</h3>
<div class="infoop5" style="margin-bottom: 25px;">
    <h4>
        <spring:message code="label.current.plan" text="Current Plan"/>: <%= eventTemplate.getTitle().getContent() %>
    </h4>
    <p>
        <%= eventTemplate.getDescription().getContent() %>
    </p>
</div>

<h4>
    <spring:message code="label.alternative.plans" text="Alternative Plans"/>
</h4>
<%
    for (final EventTemplate alternativeTemplate : new TreeSet<EventTemplate>(eventTemplate.getAlternativeEventTemplateSet())) {
%>
    <div class="infoop2"
        <h5>
            <%= alternativeTemplate.getTitle().getContent() %>
        </h5>
        <p>
            <%= alternativeTemplate.getDescription().getContent() %>
        </p>
            <a href="<%= request.getContextPath() %>/registrationSelfService/<%= dataByYear.getExternalId() %>/changePlan/<%= alternativeTemplate.getExternalId() %>"
               class="btn btn-warning">
                <spring:message code="label.select.plan" text="Select Plan"/>
            </a>
    </div>
<%
    }
%>
