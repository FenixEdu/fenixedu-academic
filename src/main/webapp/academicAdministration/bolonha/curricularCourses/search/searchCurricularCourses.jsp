<%@ page import="org.fenixedu.academic.domain.degreeStructure.Context" %><%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId" />
<bean:define id="dcpExternalId" name="degreeCurricularPlan" property="externalId" />
<bean:define id="executionYearId" name="currentExecutionYear" property="externalId" />

<h2><bean:message key="title.search.curricular.courses" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:form action="<%= String.format("/searchCurricularCourses.do?method=search&amp;dcpId=%s", dcpId) %>" >
	<fr:edit id="searchBean" name="searchBean" visible="false" />
	
	<fr:edit id="searchBean-form" name="searchBean">
		<fr:schema type="org.fenixedu.academic.ui.struts.action.manager.curricularCourses.SearchCurricularCourseBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="name" required="true" bundle="ACADEMIC_OFFICE_RESOURCES"
					 key="label.org.fenixedu.academic.ui.struts.action.manager.curricularCourses.SearchCurricularCourseBean.name"/>
			<fr:slot name="beginExecutionYear" layout="menu-select" bundle="ACADEMIC_OFFICE_RESOURCES"
					 key="label.org.fenixedu.academic.ui.struts.action.manager.curricularCourses.SearchCurricularCourseBean.beginExecutionYear">
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${name}" />
			</fr:slot>
			<fr:slot name="endExecutionYear" layout="menu-select" bundle="ACADEMIC_OFFICE_RESOURCES"
					 key="label.org.fenixedu.academic.ui.struts.action.manager.curricularCourses.SearchCurricularCourseBean.endExecutionYear">
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${name}" />
			</fr:slot>
		</fr:schema>
	
		<fr:destination name="invalid" path="<%= String.format("/searchCurricularCourses.do?method=searchInvalid&amp;dcpId=%s", dcpId) %>" />

		<fr:layout name="tabular">
		</fr:layout>
	</fr:edit>
	
	
	<p><html:submit><bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit></p>
</fr:form>

<logic:empty name="results">
	<bean:message key="message.search.curricular.courses.results.empty" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>

<logic:notEmpty name="results">
	<table class="tstyle2 thleft tdleft table">
		<thead>
			<th scope="col">
				<bean:message key="label.org.fenixedu.academic.domain.degreeStructure.Context.childDegreeModule.nameI18N" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</th>
			<th scope="col">
				<bean:message key="label.org.fenixedu.academic.domain.degreeStructure.Context.beginExecutionPeriod.qualifiedName" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</th>
			<th scope="col">
				<bean:message key="label.org.fenixedu.academic.domain.degreeStructure.Context.endExecutionPeriod.qualifiedName" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</th>
			<th scope="col">
			</th>
		</thead>
		<tbody>
			<logic:iterate name="results" id="context">
				<tr>
					<td><bean:write name="context" property="childDegreeModule.nameI18N"/></td>
					<td><bean:write name="context" property="beginExecutionPeriod.qualifiedName"/></td>
					<td>
						<logic:notEmpty name="context" property="endExecutionPeriod" >
							<bean:write name="context" property="endExecutionPeriod.qualifiedName"/>
						</logic:notEmpty>
					</td>
					<td>
						<html:link page="<%="/bolonha/curricularPlans/editCurricularCourse.faces?degreeCurricularPlanID=" + dcpExternalId +"&contextID=" + ((Context) context).getExternalId() + "&curricularCourseID=" + ((Context) context).getChildDegreeModule().getExternalId() + "&executionYearID="+ executionYearId +"&organizeBy=groups&showRules=false&hideCourses=false&action=build" %>">
							<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</tbody>
	</table>
</logic:notEmpty>
