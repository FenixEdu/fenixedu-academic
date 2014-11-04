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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>

<h2><bean:message key="label.curriculum.validation.student.enrolment.without.rules" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	
<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="externalId"/>

<p>
	<html:link page="<%= "/curriculumValidation.do?method=prepareCurriculumValidation&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
		<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>

<logic:equal name="studentCurriculumValidationAllowed" value="false">
		<bean:message key="message.curriculum.validation.not.allowed" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:equal>

<logic:equal name="studentCurriculumValidationAllowed" value="true"> 

	<fr:form action="<%= "/curriculumValidation.do?method=prepareStudentEnrolment&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
		<fr:edit id="student.enrolment.bean" name="bolonhaStudentEnrollmentBean" visible="false"/>
		
		<fr:edit id="student.enrolment.bean.execution.semester"
				 name="bolonhaStudentEnrollmentBean"
				 schema="bolonha.student.enrolment.choose.executionPeriod">
			<fr:destination name="postBack" path="<%= "/curriculumValidation.do?method=prepareStudentEnrolmentPostback&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>" />
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
	
	<bean:define id="studentCurricularPlan" name="studentCurricularPlan" type="org.fenixedu.academic.domain.StudentCurricularPlan"/>
	<bean:define id="executionSemesterId" name="bolonhaStudentEnrollmentBean" property="executionPeriod.externalId" />
	<academic:allowed operation="MANAGE_MARKSHEETS" program="<%= studentCurricularPlan.getRegistration().getDegree() %>">
		<ul class="mbottom2">
			<li>
				<html:link page="<%= "/curriculumValidation.do?method=prepareSetEvaluations&amp;studentCurricularPlanId=" + studentCurricularPlanId + "&amp;executionSemesterId=" + executionSemesterId %>">
					<bean:message key="label.curriculum.validation.continue" bundle="ACADEMIC_OFFICE_RESOURCES" />
				</html:link>
			</li>
		</ul>
	</academic:allowed>
	
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
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="warning">
				<li><span><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		</div>
	</logic:messagesPresent>
	
	<logic:messagesPresent message="true" property="error">
		<div class="error0" style="padding: 0.5em;">
		<p class="mvert0"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.enrollment.errors.in.enrolment" />:</strong></p>
		<ul class="mvert05">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
				<li><span><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		</div>
	</logic:messagesPresent>
	
	<html:form action="<%= "/curriculumValidation.do?studentCurricularPlanId=" + studentCurricularPlanId  + "&amp;executionSemesterId=" + executionSemesterId %>">
		<input type="hidden" name="method" value="enrolInDegreeModules"/>
		
		<p class="mtop15 mbottom025">
			<bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:
		</p>
		<p class="mtop025 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='prepareCurriculumValidation';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.back"/></html:submit>			
		</p>
		
		<fr:edit id="bolonhaStudentEnrolments" name="bolonhaStudentEnrollmentBean">
			<fr:layout name="bolonha-student-enrolment">
				<fr:property name="enrolmentClasses" value="se_enrolled smalltxt,se_enrolled smalltxt aright,se_enrolled nowrap smalltxt aright,se_enrolled smalltxt aright,se_enrolled aright" />
				<fr:property name="temporaryEnrolmentClasses" value="se_temporary smalltxt,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary aright" />
				<fr:property name="impossibleEnrolmentClasses" value="se_impossible smalltxt,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible aright" />
				<fr:property name="curricularCourseToEnrolClasses" value="smalltxt, nowrap smalltxt aright, nowrap smalltxt aright, aright" />				
				<fr:property name="groupRowClasses" value="se_groups" />
			</fr:layout>
		</fr:edit>
		
		<p class="mtop15 mbottom05"><bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:</p>
		<p class="mtop05 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='prepareCurriculumValidation';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.back"/></html:submit>
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