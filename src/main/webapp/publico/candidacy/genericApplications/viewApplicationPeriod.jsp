<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplication"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>
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

<%
	final IUserView userView = AccessControl.getUserView();
	if (userView != null && userView.hasRoleType(RoleType.MANAGER)) {
%>
	<br/>
	<a href="#" onclick="toggleById('#createPeriodBlock');">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.edit.application.period"/>
	</a>
	<div id="createPeriodBlock" style="display: none;">
	<br/>
	<fr:form id="genericApplicationRecommendationForm" action="/genericApplications.do" encoding="multipart/form-data">
		<input type="hidden" name="method" value="viewApplicationPeriod"/>
		<input type="hidden" name="applicationPeriodId" value="<%= genericApplicationPeriod.getExternalId() %>"/>

		<fr:edit id="genericApplicationPeriodEdit" name="applicationPeriod">
			<fr:schema type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod" bundle="CANDIDATE_RESOURCES">
				<fr:slot name="title" key="label.application.period.title" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
   					<fr:property name="size" value="50"/>
				</fr:slot>
				<fr:slot name="start" key="label.application.period.start" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="end" key="label.application.period.end" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="description" key="label.application.period.description" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="rich-text">
					<fr:property name="safe" value="true" />
					<fr:property name="columns" value="70"/>
					<fr:property name="rows" value="16"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/genericApplications.do?method=listApplicationPeriods" />
			<fr:destination name="cancel" path="/genericApplications.do?method=listApplicationPeriods" />
		</fr:edit>

		<p class="mtop15">
			<html:submit>
				<bean:message key="button.edit" bundle="APPLICATION_RESOURCES"/>
			</html:submit>
		</p>
	</fr:form>
	</div>
	<br/>
<%
	}
%>

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

<%
	if (userView != null && userView.hasRoleType(RoleType.MANAGER)) {
%>
		<table class="tstyle2 thlight thcenter mtop15">
			<tr>
				<th>
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.number"/>
				</th>
				<th>
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.full.name"/>
				</th>
				<th>
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.candidate.email"/>
				</th>
				<th width="150px;">
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.has.personal.information"/>
				</th>
				<th width="150px;">
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.number.of.documents"/>
				</th>
				<th width="150px;">
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.letters.of.recomendation.requested"/>
				</th>
				<th width="150px;">
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.letters.of.recomendation.available"/>
				</th>
				<th>
				</th>
			</tr>
			<% for (final GenericApplication genericApplication : genericApplicationPeriod.getOrderedGenericApplicationSet()) { %>
				<tr>
					<td>
						<a href="<%= request.getContextPath() + "/publico/genericApplications.do?method=viewApplication&applicationId=" + genericApplication.getExternalId() %>">
							<%= genericApplication.getApplicationNumber() %>
						</a>
					</td>
					<td>
						<%= genericApplication.getName() %>
					</td>
					<td>
						<%= genericApplication.getEmail() %>
					</td>
					<td class="center">
						<bean:message bundle="RENDERER_RESOURCES" key="<%= Boolean.valueOf(genericApplication.isAllPersonalInformationFilled()).toString().toUpperCase() %>"/>
					</td>
					<td class="center">
						<%= genericApplication.getGenericApplicationFileCount() %>
					</td>
					<td class="center">
						<%= genericApplication.getGenericApplicationRecomentationCount() %>
					</td>
					<td class="center">
						<%= genericApplication.getAvailableGenericApplicationRecomentationCount() %>
					</td>
				</tr>
			<% } %>
		</table>
<%
	}
%>
