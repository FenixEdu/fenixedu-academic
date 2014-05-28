<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplication"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.periods"/>
	<br/>
	<%= genericApplicationPeriod.getTitle().getContent() %>
</h2>

<br/>

<p class="infoop2">
<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period"/>:
<%= genericApplicationPeriod.getStart().toString("yyyy-MM-dd") %>
-
<%= genericApplicationPeriod.getEnd().toString("yyyy-MM-dd") %>
</p>

<%
	if (genericApplicationPeriod.isCurrentUserAllowedToMange()) {
%>
	<br/>
	<a href="#" onclick="toggleById('#createPeriodBlock'); toggleById('#informationBlock');">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.edit.application.period"/>
	</a>
	|
	<a href="#" onclick="toggleById('#manageMembersBlock'); toggleById('#informationBlock');">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.manage.members"/>
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
	<div id="manageMembersBlock" style="display: none;">
	<h3>
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.manage.members.list"/>
	</h3>
	<%
		if (genericApplicationPeriod.getManagerCount() == 0) {
	%>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.manage.members.none"/>
			<br/>
			<br/>
	<%
		} else {
	%>
			<ul>
				<% for (final User user : genericApplicationPeriod.getManagerSet()) { %>
					<li>
						<%= user.getPerson().getPresentationName() %>
						&nbsp;&nbsp;
						<a href="<%= request.getContextPath() +  "/publico/genericApplications.do?method=removeManager&applicationPeriodId=" + genericApplicationPeriod.getExternalId() + "&userId=" + user.getExternalId() %>">
							<bean:message key="button.remove" bundle="APPLICATION_RESOURCES"/>
						</a>
					</li>
				<% } %>
			</ul>
	<%
		}
	%>
	<fr:form id="genericApplicationRecommendationForm" action="/genericApplications.do" encoding="multipart/form-data">
		<input type="hidden" name="method" value="addManager"/>
		<input type="hidden" name="applicationPeriodId" value="<%= genericApplicationPeriod.getExternalId() %>"/>

		<fr:edit id="genericApplicationUserBean" name="genericApplicationUserBean">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationUserBean" bundle="CANDIDATE_RESOURCES">
				<fr:slot name="username" key="label.username" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
   					<fr:property name="size" value="12"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="<%= "/genericApplications.do?method=viewApplicationPeriod&applicationPeriodId=" + genericApplicationPeriod.getExternalId() %>" />
			<fr:destination name="cancel" path="<%= "/genericApplications.do?method=viewApplicationPeriod&applicationPeriodId=" + genericApplicationPeriod.getExternalId() %>" />
		</fr:edit>

		<p class="mtop15">
			<html:submit>
				<bean:message key="button.add" bundle="APPLICATION_RESOURCES"/>
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

<div id="informationBlock">
<br/>

<logic:present name="sentEmailForApplication">
	<div class="infoop success0">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.email.sent.for.confirmation"/>
	</div>
</logic:present>

<logic:notPresent name="sentEmailForApplication">
	<div id="<%= "createApplicationLink" + genericApplicationPeriod.getExternalId() %>">
	<form action="<%= request.getContextPath() + "/publico/genericApplications.do" %>">
		<input type="hidden" name="method" value="createApplicationFromPeriodPage"/>
		<input type="hidden" name="periodOid" value="<%= genericApplicationPeriod.getExternalId() %>"/>
		<input type="hidden" name="applicationPeriodId" value="<%= genericApplicationPeriod.getExternalId() %>"/>

		<bean:define id="suffix"><bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.create.application.inline"/></bean:define>
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.create.application.prefix"/>
		<input type="email" name="email" size="30" placeholder="<%= suffix %>"/>
		<html:submit>
			<bean:message key="button.send" bundle="APPLICATION_RESOURCES"/>
		</html:submit>
		<br/>
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.period.create.application.suffix"/>
	</form>
	</div>

	<div id="<%= "recreateApplicationLink" + genericApplicationPeriod.getExternalId() %>">
		<br/>
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.forgot.confirmation.code"/>
		<html:link  href="#" onclick="<%= "toggleById('#createApplicationForm" + genericApplicationPeriod.getExternalId() + "');" %>">
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

<logic:messagesPresent message="true">
     <html:messages id="messages" message="true" bundle="CANDIDATE_RESOURCES">
      <p><span class="error0"><bean:write name="messages" filter="false" /></span></p>
     </html:messages>
</logic:messagesPresent>
   
<%
	if (genericApplicationPeriod.isCurrentUserAllowedToMange()) {
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

</div>

<logic:present name="changedManagerList">
	<script>
		toggleById('#manageMembersBlock');
		toggleById('#informationBlock');
	</script>
</logic:present>
