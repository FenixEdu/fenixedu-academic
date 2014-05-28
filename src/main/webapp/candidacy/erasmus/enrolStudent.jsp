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
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess" %>

<logic:equal name="restrictEnrollment" value="false">
	<em>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice"/>
	</em>
	<h2>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.student.enrollment.courses"/>
	</h2>	
	<div class="error0">
		<bean:message bundle="INTERNATIONAL_RELATIONS_OFFICE"  key="label.enrolstudent.nullcandidacy"/>
	</div>
		<bean:define id="process" name="process" />
		<bean:define id="action" name="action"/>
		<html:form action="<%= action.toString() %>" method="POST">
			<html:hidden property="withRules" value="false"/>
			<html:hidden property="method" value="doEnrol"/>
			<html:hidden property="processId" value="<%= ((MobilityIndividualApplicationProcess) process).getExternalId().toString() %>"/>
			<p class="mtop025 mbottom1">		
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value = 'listProcessAllowedActivities'"><bean:message bundle="APPLICATION_RESOURCES"  key="label.back"/></html:submit>			
			</p>
		</html:form>

</logic:equal>

<logic:equal name="restrictEnrollment" value="true">

		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
		<h2>
			<bean:write name="bolonhaStudentEnrollmentBean"  property="funcionalityTitle" />
		</h2>
	
		<bean:define id="periodSemester" name="bolonhaStudentEnrollmentBean" property="executionPeriod.semester" />
		<bean:define id="executionYearName" name="bolonhaStudentEnrollmentBean" property="executionPeriod.executionYear.year" />
		<bean:define id="process" name="process" />
	
	
	<fr:form action="<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=enrolStudent&processId=" + ((MobilityIndividualApplicationProcess) process).getExternalId() %>">
		
	
		<fr:edit id="studentEnrolment"
				 name="bolonhaStudentEnrollmentBean" schema="xpto">
				
			<fr:destination name="postBack" path="/caseHandlingMobilityIndividualApplicationProcess.do?method=postBack"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value=""/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
	
		<p class="mtop0 mbottom15">
			<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.registration"/>:</strong> <bean:write name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.degreeCurricularPlan.presentationName"/> 
		</p>
		
		<bean:define id="student" name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.registration.student" />
		
		<logic:messagesPresent message="true" property="success">
			<div class="success0" style="padding: 0.5em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="success">
				<span><bean:write name="messages" /></span>
			</html:messages>
			</div>
		</logic:messagesPresent>
	
		<logic:messagesPresent message="true" property="warning" >
			<div class="warning0" style="padding: 0.5em;">
			<p class="mvert0"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.enrollment.warnings.in.enrolment" />:</strong></p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="warning">
					<li><span><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
			</div>
		</logic:messagesPresent>
		
		<logic:messagesPresent message="true" property="error">
			<div class="error0" style="padding: 0.5em;">
			<p class="mvert0"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.enrollment.errors.in.enrolment" />:</strong></p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
					<li><span><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
			</div>
		</logic:messagesPresent>
		
		<bean:define id="action" name="action"/>
		<html:form action="<%= action.toString() %>" method="POST">
			<html:hidden property="withRules" value="false"/>
			<html:hidden property="method" value="doEnrol"/>
			<html:hidden property="processId" value="<%= ((MobilityIndividualApplicationProcess) process).getExternalId().toString() %>"/>
			
			<p class="mtop15 mbottom025">
				<bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:
			</p>
			<p class="mtop025 mbottom1">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>			
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value = 'listProcessAllowedActivities'"><bean:message bundle="APPLICATION_RESOURCES"  key="label.back"/></html:submit>			
			</p>
			
			<fr:edit id="bolonhaStudentEnrolments" name="bolonhaStudentEnrollmentBean">
				<fr:layout name="erasmus-bolonha-student-enrolment">
					<fr:property name="enrolmentClasses" value="se_enrolled smalltxt,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled aright" />
					<fr:property name="temporaryEnrolmentClasses" value="se_temporary smalltxt,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary aright" />
					<fr:property name="impossibleEnrolmentClasses" value="se_impossible smalltxt,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible aright" />
					<fr:property name="curricularCourseToEnrolClasses" value="smalltxt, smalltxt aright, smalltxt aright, aright" />				
					<fr:property name="groupRowClasses" value="se_groups" />
				</fr:layout>
			</fr:edit>
			
			<p class="mtop15 mbottom05"><bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:</p>
			<p class="mtop05 mbottom1">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
			</p>
		
		</html:form>
	
	
	
	<p class="mtop2 mbottom0"><em><bean:message bundle="APPLICATION_RESOURCES" key="label.legend"/>:</em></p>
	
	<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.minCredits" bundle="APPLICATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.creditsConcluded" bundle="APPLICATION_RESOURCES"/></em></p>
	<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.maxCredits" bundle="APPLICATION_RESOURCES"/></em></p>
	
	<table class="mtop0">
	<tr>
		<td><div style="width: 10px; height: 10px; border: 1px solid #84b181; background: #eff9ee; float:left;"></div></td>
		<td><bean:message bundle="APPLICATION_RESOURCES"  key="label.confirmedEnrollments"/><span class="color888"> (<bean:message bundle="APPLICATION_RESOURCES"  key="label.greenLines"/>)</span></td>
	</tr>
	<tr>
		<td><div style="width: 10px; height: 10px; border: 1px solid #b9b983; background: #fafce6; float:left;"></div></td>
		<td><bean:message bundle="APPLICATION_RESOURCES"  key="label.temporaryEnrollments"/><span class="color888"> (<bean:message bundle="APPLICATION_RESOURCES"  key="label.yellowLines"/>)</span></td>
	</tr>
	<tr>
		<td><div style="width: 10px; height: 10px; border: 1px solid #be5a39; background: #ffe9e2; float:left;"></div></td>
		<td><bean:message bundle="APPLICATION_RESOURCES"  key="label.impossibleEnrollments"/><span class="color888"> (<bean:message bundle="APPLICATION_RESOURCES"  key="label.redLines"/>)</span></td>
	</tr>
	</table>
	
</logic:equal>
