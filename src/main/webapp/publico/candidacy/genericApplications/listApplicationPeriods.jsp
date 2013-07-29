<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod"%>
<%@page import="java.util.SortedSet"%>

<html:xhtml/>

<script>
   	function toggleById(id) {
   		$(id).toggle();
	}
</script>


<h2>
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.periods"/>
</h2>

<logic:present name="sentEmailForApplication">
	<div class="infoop success0">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.email.sent.for.confirmation"/>
	</div>
</logic:present>

<%
	final SortedSet<GenericApplicationPeriod> periods = (SortedSet<GenericApplicationPeriod>) request.getAttribute("periods");
	if (periods.isEmpty()) {
%>
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.periods.none"/>
<%
	} else {
%>
		<table class="tstyle2 thlight thcenter mtop15">
			<tr>
				<td>
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period"/>
				</td>
				<td>
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.title"/>
				</td>
				<td>
				</td>
			</tr>
<%
	    for (final GenericApplicationPeriod period : periods) {
%>
			<tr>
				<td>
					<%= period.getStart().toString("yyyy-MM-dd") %>
					-
					<%= period.getEnd().toString("yyyy-MM-dd") %>
				</td>
				<td style="font-weight: bold;">
					<a href="<%= request.getContextPath() + "/publico/genericApplications.do?method=viewApplicationPeriod&applicationPeriodId=" + period.getExternalId() %>">
						<%= period.getTitle().getContent() %>
					</a>
				</td>
				<td>
					<span id="<%= "createApplicationLink" + period.getExternalId() %>">
					<html:link  href="#" onclick="<%= "toggleById('#createApplicationLink" + period.getExternalId() + "');toggleById('#createApplicationForm" + period.getExternalId() + "');" %>">
						<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.create.application"/>
					</html:link>
					</span>
					<form id="<%= "createApplicationForm" + period.getExternalId() %>" action="<%= request.getContextPath() + "/publico/genericApplications.do" %>"
							style="display: none;">
						<input type="hidden" name="method" value="createApplication"/>
						<input type="hidden" name="periodOid" value="<%= period.getExternalId() %>"/>

						<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.email.for.registry"/>
						<input type="email" name="email" size="30"/>
						<html:submit>
							<bean:message key="button.send" bundle="APPLICATION_RESOURCES"/>
						</html:submit>
					</form>
				</td>
			</tr>
<%
	    }
%>
		</table>
<%
	}
%>


<logic:present name="genericApplicationPeriodBean">
	<br/>
	<br/>
	<a href="#" onclick="toggleById('#createPeriodBlock');">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.create.application.period"/>
	</a>
	<div id="createPeriodBlock" style="display: none;">
	<br/>
	<fr:form id="genericApplicationPeriodBeanForm" action="/genericApplications.do?method=createApplicationPeriod"
			encoding="multipart/form-data">

 		<h2><bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.create.application.period"/></h2>

		<fr:edit id="genericApplicationPeriodBean" name="genericApplicationPeriodBean">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationPeriodBean" bundle="CANDIDATE_RESOURCES">
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
				<bean:message key="button.create" bundle="APPLICATION_RESOURCES"/>
			</html:submit>
		</p>
	</fr:form>
	</div>
</logic:present>
