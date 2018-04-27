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

<%@page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<p>
	<h2>
		<bean:message key="title.execution.course.merge" bundle="SOP_RESOURCES"/>
	</h2>
</p>

<logic:equal name="previousOrEqualSemester" value="true">
	<div class="error0">
		<bean:write name="degreeBean" property="academicInterval.pathName"/>
	</div>
</logic:equal>
<logic:equal name="previousOrEqualSemester" value="false">
	<bean:write name="degreeBean" property="academicInterval.pathName"/>
</logic:equal>

<logic:messagesPresent message="true" property="error">
	<br />
	<html:messages id="messages" message="true" bundle="SOP_RESOURCES" property="error">
		<div class="error2"><bean:write name="messages"/></div>
	</html:messages>
	<br />
</logic:messagesPresent>

<span class="error"><!-- Error messages go here --><html:errors/></span>

<html:form action="/chooseDegreesForExecutionCourseMerge.do?method=mergeExecutionCourses" styleId="submitForm">
	<p>
		<html:link page="/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
	</br>
	(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mergedegrees.source"/>) <br/>
	[<bean:write name="degreeBean" property="sourceDegree.code" />] <bean:write name="degreeBean" property="sourceDegree.sigla" /> - <bean:write name="degreeBean" property="sourceDegree.presentationName"/>
	
	

	<fr:edit id="degreeBean" name="degreeBean">
		<fr:schema type="org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement.MergeExecutionCourseDA$DegreesMergeBean" bundle="ACADEMIC_OFFICE_RESOURCES">				
			<fr:slot name="source" layout="menu-select" key="label.mergedegrees.mergecourses.source" required="true">
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.SourceExecutionCoursesProvider"/>
				<fr:property name="format" value="${sourcePresentationName}" />
			</fr:slot>
			<fr:slot name="destination" layout="menu-select" key="label.mergedegrees.mergecourses.destination" required="true">
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DestinationExecutionCoursesProvider"/>
				<fr:property name="format" value="${destinationPresentationName}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle0 thleft mtop15"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<br/>
	[<bean:write name="degreeBean" property="destinationDegree.code" />] <bean:write name="degreeBean" property="destinationDegree.sigla"/> - <bean:write name="degreeBean" property="destinationDegree.presentationName"/> <br/>
	(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mergedegrees.destination"/>)
	<br/>
	<br/>
	<bean:define id="deleteConfirm">
		requestConfirmation('submitForm','<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="message.mergedegrees.mergecourses.confirmation"/>','<bean:message bundle="HTMLALT_RESOURCES" key="submit.confirm"/>');return false;
	</bean:define>	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="<%= deleteConfirm %>">
		<bean:message bundle="MANAGER_RESOURCES" key="button.continue"/>
	</html:submit>
</html:form>


<script src="../javaScript/alertHandlers.js"></script>
<script src="../javaScript/jquery.alerts.js"></script>
