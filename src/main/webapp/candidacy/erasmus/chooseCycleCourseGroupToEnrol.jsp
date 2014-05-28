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
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess" %>

	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
	<bean:define id="cycleTypeToEnrolQualifiedName" name="cycleEnrolmentBean" property="cycleTypeToEnrol.qualifiedName" />
	<h2><bean:message key="label.student.enrollment.enrolIn" bundle="ACADEMIC_OFFICE_RESOURCES" /> <bean:message  key="<%=cycleTypeToEnrolQualifiedName.toString()%>" bundle="ENUMERATION_RESOURCES"/></h2>
	
	
	<bean:define id="studentCurricularPlanId" name="cycleEnrolmentBean" property="studentCurricularPlan.externalId" />
	<bean:define id="registrationId" name="cycleEnrolmentBean" property="studentCurricularPlan.registration.externalId" />
	<bean:define id="executionPeriodId" name="cycleEnrolmentBean" property="executionPeriod.externalId" />
	<bean:define id="withRules" name="withRules" />
	<bean:define id="process" name="process" />  
	
	<logic:empty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<span class="error0">
			<bean:message  key="label.student.enrollment.cycleCourseGroup.noCycleDestinationAffinities" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</span>
		<br/><br/>
		<html:form action="<%="/caseHandlingMobilityIndividualApplicationProcess.do?method=cancelChooseCycleCourseGroupToEnrol&amp;scpID=" + studentCurricularPlanId.toString() + "&amp;executionPeriodID=" + executionPeriodId.toString() + "&amp;withRules=" + withRules.toString()%>">
			<html:submit altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
				<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:submit>
		</html:form>
	</logic:empty>
	
	<logic:notEmpty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
	<html:form action="<%="/caseHandlingMobilityIndividualApplicationProcess.do?scpID=" + studentCurricularPlanId.toString() + "&amp;executionPeriodID=" + executionPeriodId.toString() + "&amp;withRules=" + withRules.toString()%>">
	
		<input type="hidden" name="method" />
		<input type="hidden" name="withRules" value="<%=withRules.toString()%>"/>
		<input type="hidden" name="processId" value="<%= ((MobilityIndividualApplicationProcess) process).getExternalId().toString() %>"/>
		<logic:messagesPresent message="true">
			<div class="error0" style="padding: 0.5em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<span><bean:write name="messages" /></span>
			</html:messages>
			</div>
		</logic:messagesPresent>
		
		<fr:edit id="cycleEnrolmentBean" 
				 name="cycleEnrolmentBean" 
				 schema="CycleEnrolmentBean.chooseCycleCourseGroupToEnrol">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/bolonhaStudentEnrollment.do?method=enrolInCycleCourseGroupInvalid" />
		</fr:edit>
		
		<table>
			<tr>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInCycleCourseGroup';"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
				</td>
				<td>
					<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='cancelChooseCycleCourseGroupToEnrol';"><bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/></html:cancel>
				</td>
			</tr>
		</table>
		
	</html:form>
	</logic:notEmpty>
