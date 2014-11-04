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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>


<fr:form action="/editExecutionCourseChooseExPeriod.do?method=secondPrepareEditExecutionCourse">
	<fr:edit id="sessionBeanJSP" name="sessionBean">
		<fr:schema type="org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement.ExecutionCourseBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="executionSemester" layout="menu-select" key="label.manager.executionCourseManagement.executionPeriod" required="true">
				<fr:property name="format" value="${qualifiedName}"/>
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.NotClosedExecutionPeriodsProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/>
	</html:submit>
</fr:form>

