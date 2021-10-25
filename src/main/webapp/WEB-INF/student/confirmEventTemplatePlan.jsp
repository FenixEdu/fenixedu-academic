<%@ page import="com.google.gson.JsonObject" %>
<%@ page import="org.fenixedu.academic.domain.Degree" %>
<%@ page import="org.fenixedu.academic.domain.accounting.EventTemplate" %>
<%@ page import="org.fenixedu.academic.domain.student.Registration" %>
<%@ page import="org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear" %>
<%@ page import="com.google.gson.JsonElement" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.fenixedu.academic.util.Money" %>
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
    final EventTemplate selectedEventTemplate = (EventTemplate) request.getAttribute("eventTemplate");
    final JsonObject json = selectedEventTemplate.getConfigFor(dataByYear.getExecutionYear().getBeginLocalDate().plusWeeks(4).toDateTimeAtStartOfDay())
            .getConfig();
    final JsonObject tuitionMap = json.getAsJsonObject(EventTemplate.Type.TUITION.name()).getAsJsonObject("dueDateAmountMap");
    final JsonObject insuranceMap = json.getAsJsonObject(EventTemplate.Type.INSURANCE.name()).getAsJsonObject("dueDateAmountMap");
    final JsonObject adminFeesMap = json.getAsJsonObject(EventTemplate.Type.ADMIN_FEES.name()).getAsJsonObject("dueDateAmountMap");
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
    <spring:message code="label.confirm.change.plans" text="Confirm Change Plan"/>
</h4>

<form method="post" class="infoop2"
      action="<%= request.getContextPath() %>/registrationSelfService/<%= dataByYear.getExternalId() %>/changePlan/<%= selectedEventTemplate.getExternalId() %>">
    ${csrf.field()}
    <h5>
        <%= selectedEventTemplate.getTitle().getContent() %>
    </h5>
    <p>
        <%= selectedEventTemplate.getDescription().getContent() %>
    </p>
    <p>
        <h6>
            <spring:message code="label.tuition.value" text="Tuition"/>:
        </h6>
        <ul>
            <% for (final Map.Entry<String, JsonElement> e : tuitionMap.entrySet()) { %>
                <li>
                    <%= e.getKey() %> : <%= new Money(e.getValue().getAsString()).toString() %>
                    <spring:message code="label.euro" text="Euro"/>
                </li>
            <% } %>
        </ul>
        <h6>
            <spring:message code="label.insurance.value" text="Insurance"/>:
        </h6>
        <ul>
            <% for (final Map.Entry<String, JsonElement> e : insuranceMap.entrySet()) { %>
            <li>
                <%= e.getKey() %> : <%= new Money(e.getValue().getAsString()).toString() %>
                <spring:message code="label.euro" text="Euro"/>
            </li>
            <% } %>
        </ul>
        <h6>
            <spring:message code="label.adminFees.value" text="Administrative Fees"/>:
        </h6>
        <ul>
            <% for (final Map.Entry<String, JsonElement> e : adminFeesMap.entrySet()) { %>
            <li>
                <%= e.getKey() %> : <%= new Money(e.getValue().getAsString()).toString() %>
                <spring:message code="label.euro" text="Euro"/>
            </li>
            <% } %>
        </ul>
    </p>
    <p>
        <input type="checkbox" onclick="acceptChanges();" id="acceptCheckbox">
        <spring:message code="label.change.plan.confirm.text" text="I accept"/>
    </p>
    <button type="submit" class="btn btn-primary" disabled id="changePlanButton"
            onclick='return confirm("<spring:message code="label.confirm.change.plan.button" text="Continue"/>");' >
        <spring:message code="label.change.plan" text="Change Plan"/>
    </button>
</form>

<script type="text/javascript">
    
    function acceptChanges() {
        document.getElementById("changePlanButton").disabled = !document.getElementById('acceptCheckbox').checked;
    }
    
</script>