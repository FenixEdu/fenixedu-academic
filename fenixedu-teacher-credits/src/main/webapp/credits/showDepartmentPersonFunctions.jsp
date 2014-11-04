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
<%@ page isELIgnored="true"%>
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<html:messages id="message" message="true">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>


<logic:present name="departmentCreditsBean">

	<fr:form action="/managePersonFunctionsShared.do?method=showDepartmentPersonFunctions">
		<fr:edit id="departmentCreditsBean" name="departmentCreditsBean">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.DepartmentCreditsBean">
				<fr:slot name="department" key="label.department" layout="menu-select">
					<fr:property name="from" value="availableDepartments"/>
					<fr:property name="format" value="${name}"/>
				</fr:slot>
				<fr:slot name="executionSemester" key="label.execution-period" layout="menu-select" required="true">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionSemestersProvider" />
					<fr:property name="format" value="${executionYear.year} - ${semester}º semestre" />
					<fr:property name="nullOptionHidden" value="true" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="cancel" path="/exportCredits.do?method=exportDepartmentPersonFunctions" />
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message key="label.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.export" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</fr:form>
	<logic:notEmpty name="departmentCreditsBean" property="departmentPersonFunctions">
		<bean:define id="canShowCredits" value="false" type="java.lang.String"/>
		<logic:present name="canViewCredits">
			<bean:define id="canShowCredits" name="canViewCredits" type="java.lang.String"/>
		</logic:present>
		<table class="tstyle2 thlight thleft mtop05 mbottom05">
			<tr>
				<th><bean:message key="label.teacher.id" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.name" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.beginDate" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.endDate" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<logic:equal name="canShowCredits" value="true">
					<th><bean:message key="label.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th><bean:message key="label.managementPosition.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</logic:equal>
			</tr>
			<logic:iterate id="personFunction" name="departmentCreditsBean" property="departmentPersonFunctions">
				<tr>
					<td><bean:write name="personFunction" property="person.username"/></td>
					<td><bean:write name="personFunction" property="person.name"/></td>
					<td><bean:write name="personFunction" property="function.name"/></td>
					<td><bean:write name="personFunction" property="function.unit.presentationName"/></td>
					<td><bean:write name="personFunction" property="beginDate"/></td>
					<td><bean:write name="personFunction" property="endDate"/></td>
					<logic:equal name="canShowCredits" value="true">
						<% if(personFunction instanceof org.fenixedu.academic.domain.organizationalStructure.PersonFunctionShared){ %>
							<td align="center"><bean:write name="personFunction" property="percentage"/></td>
						<% } else { %>
							<td align="center">-</td>
						<% }%>
						<%-- --%>
						<td><bean:write name="personFunction" property="credits"/>--><bean:write name="personFunction" property="externalId"/></td>
					</logic:equal>
					<logic:notEqual name="canShowCredits" value="true">
						<td/><td/>
					</logic:notEqual>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>