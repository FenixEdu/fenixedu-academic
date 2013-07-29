<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod"%>
<%@page import="java.util.SortedSet"%>

<html:xhtml/>

<% final GenericApplicationPeriod genericApplicationPeriod = (GenericApplicationPeriod) request.getAttribute("applicationPeriod"); %>

<script>
   	function toggleById(id) {
   		$(id).toggle();
	}
</script>

<h2>
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.periods"/>: <%= genericApplicationPeriod.getTitle().getContent() %>
</h2>

<br/>

<p class="infoop2">
<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period"/>:
<%= genericApplicationPeriod.getStart().toString("yyyy-MM-dd") %>
-
<%= genericApplicationPeriod.getEnd().toString("yyyy-MM-dd") %>
</p>

<br/>

<div class="infoop">
	<%= genericApplicationPeriod.getDescription() %>
</div>

<br/>

<logic:present name="sentEmailForApplication">
	<div class="infoop success0">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.email.sent.for.confirmation"/>
	</div>
</logic:present>

<logic:notPresent name="sentEmailForApplication">
	<div id="<%= "createApplicationLink" + genericApplicationPeriod.getExternalId() %>">
		<html:link  href="#" onclick="<%= "toggleById('#createApplicationLink" + genericApplicationPeriod.getExternalId() + "');toggleById('#createApplicationForm" + genericApplicationPeriod.getExternalId() + "');toggleById('#recreateApplicationLink" + genericApplicationPeriod.getExternalId() + "');" %>">
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.create.application"/>
		</html:link>
	</div>

	<div id="<%= "recreateApplicationLink" + genericApplicationPeriod.getExternalId() %>">
		<br/>
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.forgot.confirmation.code"/>
		<html:link  href="#" onclick="<%= "toggleById('#createApplicationLink" + genericApplicationPeriod.getExternalId() + "');toggleById('#createApplicationForm" + genericApplicationPeriod.getExternalId() + "');toggleById('#recreateApplicationLink" + genericApplicationPeriod.getExternalId() + "');" %>">
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.forgot.confirmation.code.here"/>
		</html:link>
		.
	</div>

	<form id="<%= "createApplicationForm" + genericApplicationPeriod.getExternalId() %>" action="<%= request.getContextPath() + "/publico/genericApplications.do" %>"
			style="display: none;">
		<input type="hidden" name="method" value="createApplicationFromPeriodPage"/>
		<input type="hidden" name="periodOid" value="<%= genericApplicationPeriod.getExternalId() %>"/>
		<input type="hidden" name="applicationPeriodId" value="<%= genericApplicationPeriod.getExternalId() %>"/>

		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.email.for.registry"/>
		<input type="email" name="email" size="30"/>
		<html:submit>
			<bean:message key="button.send" bundle="APPLICATION_RESOURCES"/>
		</html:submit>
	</form>
</logic:notPresent>