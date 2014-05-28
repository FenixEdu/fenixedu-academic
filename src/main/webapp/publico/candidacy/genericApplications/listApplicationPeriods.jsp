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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
