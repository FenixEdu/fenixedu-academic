<%--

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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES"
	key="label.manager.specialSeason.specialSeasonStatusTracker" /></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<fr:form id="searchForm" action="/specialSeason/specialSeasonStatusTracker.do?method=listStudents">
	<fr:edit id="bean" name="bean">
		<fr:schema type="org.fenixedu.academic.ui.struts.action.manager.enrolments.SpecialSeasonStatusTrackerBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="evaluationSeason" layout="menu-select" key="label.evaluationSeason" required="true">
				<fr:property name="format" value="${name.content}"/>
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.EvaluationSeasonProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
			<fr:slot name="executionSemester" layout="menu-select" key="label.executionSemester" required="true">
				<fr:property name="format" value="${qualifiedName}"/>
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionSemestersProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
			<fr:slot name="department" layout="menu-select-postback" key="label.department">
				<fr:property name="format" value="${name}"/>
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DepartmentsProvider"/>
				<fr:property name="saveOptions" value="true"/>
				<fr:property name="destination" value="postback"/>
			</fr:slot>
			<fr:slot name="competenceCourse" layout="menu-select" key="label.competenceCourse">
				<fr:property name="format" value="${name}"/>
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.CompetenceCoursesForDepartmentProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:destination name="postback" path="/specialSeason/specialSeasonStatusTracker.do?method=updateDepartmentSelection" />
			<fr:property name="classes" value="tstyle5 thmiddle thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="MANAGER_RESOURCES" key="button.show"/>
	</html:submit>
</fr:form>

<fr:form id="beanForm" action="/specialSeason/specialSeasonStatusTracker.do?method=exportXLS">
	<fr:edit id="bean" name="bean" visible="false"/>
</fr:form>
<p class="mtop15 mbottom1">
	<a href="javascript:var form = document.getElementById('beanForm');form.method.value='exportXLS';form.submit()">
		<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
		<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</a>
</p>

<fr:view name="bean" property="entries">
	<fr:schema type="org.fenixedu.academic.ui.struts.action.manager.enrolments.SpecialSeasonStatusTrackerRegisterBean" bundle="MANAGER_RESOURCES">
		<fr:slot name="studentNumber" key="specialSeason.label.studentNumber"/>
		<fr:slot name="studentName" key="specialSeason.label.studentName"/>
		<fr:slot name="degreeSigla" key="specialSeason.label.degreeSigla"/>
		<fr:slot name="courseName" key="specialSeason.label.courseName"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thleft" />
		<fr:property name="columnClasses" value=",,,,,,tdclear tderror1" />
	</fr:layout>		
</fr:view>
