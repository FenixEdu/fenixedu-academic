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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="role(MANAGER)">
	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
	
	<bean:define id="cycleTypeToEnrolQualifiedName" name="cycleEnrolmentBean" property="cycleTypeToEnrol.qualifiedName" />
	<h2><bean:message key="label.student.enrollment.enrolIn" bundle="ACADEMIC_OFFICE_RESOURCES" /> <bean:message  key="<%=cycleTypeToEnrolQualifiedName.toString()%>" bundle="ENUMERATION_RESOURCES"/></h2>
	
	
	<bean:define id="studentCurricularPlanId" name="cycleEnrolmentBean" property="studentCurricularPlan.externalId" />
	
	<logic:empty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<span class="error0">
			<bean:message  key="label.student.enrollment.cycleCourseGroup.noCycleDestinationAffinities" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</span>
		<br/><br/>
		<fr:form action="<%="/bolonhaStudentEnrolment.do?method=prepareShowDegreeModulesToEnrol&amp;scpId=" + studentCurricularPlanId.toString()%>">
			<html:cancel altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
				<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:cancel>
		</fr:form>
	</logic:empty>
	
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
				 action="/bolonhaStudentEnrolment.do?method=enrolInCycleCourseGroup">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/bolonhaStudentEnrolment.do?method=enrolInCycleCourseGroupInvalid" />
			<fr:destination name="cancel" path="<%="/bolonhaStudentEnrolment.do?method=prepareShowDegreeModulesToEnrol&scpId=" + studentCurricularPlanId.toString()%>"/>
		</fr:edit>
	</logic:notEmpty>
</logic:present>

