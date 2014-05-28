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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="role(STUDENT)">
	<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
	<bean:define id="cycleTypeToEnrolQualifiedName" name="cycleEnrolmentBean" property="cycleTypeToEnrol.qualifiedName" />
	<h2><bean:message key="label.enrollment.enrolIn" bundle="STUDENT_RESOURCES" /> <bean:message  key="<%=cycleTypeToEnrolQualifiedName.toString()%>" bundle="ENUMERATION_RESOURCES"/></h2>
	
	<bean:define id="registrationId" name="cycleEnrolmentBean" property="studentCurricularPlan.registration.externalId" />
	
	<logic:empty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<span class="error0">
			<bean:message  key="label.enrollment.cycleCourseGroup.noCycleDestinationAffinities" bundle="STUDENT_RESOURCES"/>
		</span>
		<br/><br/>
		<fr:form action="<%="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=" + registrationId%>">
			<html:cancel altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
				<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:cancel>
		</fr:form>
	</logic:empty>
	
	<p class="warning0"><strong><bean:message key="label.showAffinityToEnrol.choice.message" bundle="APPLICATION_RESOURCES" /></strong></p>
	<logic:notEmpty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<logic:messagesPresent message="true">
			<div class="error0" style="padding: 0.5em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<span><bean:write name="messages" /></span>
			</html:messages>
			</div>
		</logic:messagesPresent>
		
		<fr:edit id="cycleEnrolmentBean" 
				 name="cycleEnrolmentBean" 
				 schema="CycleEnrolmentBean.chooseCycleCourseGroupToEnrol"
				 action="/bolonhaStudentEnrollment.do?method=enrolInCycleCourseGroup">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/bolonhaStudentEnrollment.do?method=enrolInCycleCourseGroupInvalid" />
			<fr:destination name="cancel" path="<%="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=" + registrationId%>"/>
		</fr:edit>
	</logic:notEmpty>
</logic:present>

