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
<%@ page import="org.apache.struts.action.ActionMessages" %>

<%@page import="net.sourceforge.fenixedu.domain.CompetenceCourse"%>
<html:xhtml />

<logic:present role="role(STUDENT)">

	<h2><bean:message key="label.phd.student.enrolments" bundle="PHD_RESOURCES" /></h2>

	<bean:define id="periodSemester" name="bolonhaStudentEnrollmentBean" property="executionPeriod.semester" />
	<bean:define id="executionYearName" name="bolonhaStudentEnrollmentBean" property="executionPeriod.executionYear.year" />

	<p class="mtop15 mbottom025">
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.executionPeriod"/>:</strong> <bean:message bundle="STUDENT_RESOURCES"  key="label.periodDescription" arg0="<%=periodSemester.toString()%>" arg1="<%=executionYearName.toString()%>" />
	</p>
	<p class="mtop0 mbottom15">
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.registration.basic"/>:</strong> <bean:write name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.degreeCurricularPlan.presentationName"/> 
	</p>
	
	<ul class="mbottom15">
		<li>
			<html:link action="/phdStudentEnrolment.do?method=showEnrollmentInstructions" styleClass="externallink" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewInstructions"/></html:link>
		</li>
		<li>
			<bean:define id="studentCurricularPlan" name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan" />

			<bean:define id="degreeId" name="studentCurricularPlan" property="degree.externalId" />
			<bean:define id="degreeCurricularPlanId" name="studentCurricularPlan" property="degreeCurricularPlan.externalId" />
			<bean:define id="executionPeriodId" name="bolonhaStudentEnrollmentBean" property="executionPeriod.externalId" />
			
			<html:link href="<%=request.getContextPath() + "/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?organizeBy=groups&amp;showRules=false&amp;hideCourses=false&amp;degreeID=" + degreeId + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanId + "&amp;executionPeriodOID=" + executionPeriodId %>" styleClass="externallink" target="_blank">
				<bean:message bundle="STUDENT_RESOURCES"  key="label.viewDegreeCurricularPlan"/>
			</html:link>
		</li>
		<li>
			<html:link action="/viewStudentCurriculum.do?method=prepare" paramId="registrationOID" paramName="studentCurricularPlan" paramProperty="registration.externalId" styleClass="externallink" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewStudentCurricularPlan"/></html:link>
		</li>
		<li>
			<html:link href="mailto:nucleo.graduacao@ist.utl.pt" styleClass="externallink">
				<bean:message key="link.academicSupport" bundle="GLOBAL_RESOURCES"/>
			</html:link>
		</li>
	</ul>

	<div class="infoop2 mtop05 mbottom15">
		<p><span><bean:message bundle="APPLICATION_RESOURCES" key="label.warning.coursesAndGroupsSimultaneousEnrolment"/></span></p>
	</div>
	
	<logic:notEmpty name="competenceCoursesAvailableToEnrol">
		<strong><bean:message key="label.phd.studyPlan.competence.courses" bundle="PHD_RESOURCES" /></strong>
		<fr:view name="competenceCoursesAvailableToEnrol">

			<fr:schema bundle="PHD_RESOURCES" type="<%= CompetenceCourse.class.getName() %>">
				<fr:slot name="departmentUnit.name" />
				<fr:slot name="name" />
				<fr:slot name="ectsCredits" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10" />
				<fr:property name="columnClasses" value=",,acenter" />
			</fr:layout>
		</fr:view>
		<em>- disciplinas disponíveis para inscrição</em>
	</logic:notEmpty>

	<logic:messagesPresent message="true" property="success">
		<p>
		<span class="success0" style="padding: 0.25em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="success">
				<span><bean:write name="messages" /></span>
			</html:messages>
		</span>
		</p>
	</logic:messagesPresent>
	
		<logic:messagesPresent message="true" property="warning" >
		<div class="warning0" style="padding: 0.5em;">
		<ul class="mvert05">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="warning">
				<li><span><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		</div>
	</logic:messagesPresent>

	<logic:messagesPresent message="true" property="error">
		<div class="error0 mvert1" style="padding: 0.5em;">
			<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.errors.in.enrolment" />:</strong></p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
					<li><span><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
		</div>
	</logic:messagesPresent>
	
	<fr:form action="/phdStudentEnrolment.do">
		<input type="hidden" name="method" />
		
		<p class="mtop15 mbottom025">
			<bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:
		</p>
		<p class="mtop025 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
		</p>
		
		<fr:edit id="bolonhaStudentEnrolments" name="bolonhaStudentEnrollmentBean">
			<fr:layout name="bolonha-student-enrolment">
				<fr:property name="enrolmentClasses" value="se_enrolled smalltxt,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled aright" />
				<fr:property name="temporaryEnrolmentClasses" value="se_temporary smalltxt,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary aright" />
				<fr:property name="impossibleEnrolmentClasses" value="se_impossible smalltxt,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible aright" />
				<fr:property name="curricularCourseToEnrolClasses" value="smalltxt, smalltxt aright, smalltxt aright, aright" />				
				<fr:property name="groupRowClasses" value="se_groups" />

				<fr:property name="encodeGroupRules" value="true" />
				<fr:property name="encodeCurricularRules" value="true" />
			</fr:layout>
		</fr:edit>
		
		
		<p class="mtop15 mbottom05"><bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:</p>
		<p class="mtop05 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
		</p>
	
	</fr:form>

	<p class="mtop15">
	<em><bean:message bundle="STUDENT_RESOURCES"  key="message.enrollment.terminated"/> <html:link action="/viewStudentCurriculum.do?method=prepare" paramId="registrationOID" paramName="studentCurricularPlan" paramProperty="registration.externalId"><bean:message bundle="STUDENT_RESOURCES"  key="message.student.curriculum"/></html:link>.</em> <br/>
	<em><bean:message bundle="STUDENT_RESOURCES"  key="message.enrollment.terminated.shifts"/> <html:link page="/studentShiftEnrollmentManager.do?method=prepare" titleKey="link.title.shift.enrolment"><bean:message key="link.shift.enrolment"/></html:link>.</em>
	</p>
	
	
	<p class="mtop2 mbottom0"><em><bean:message bundle="APPLICATION_RESOURCES"  key="label.legend"/>:</em></p>
	
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

</logic:present>
