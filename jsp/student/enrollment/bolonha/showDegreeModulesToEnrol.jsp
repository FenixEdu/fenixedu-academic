<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<logic:present role="STUDENT">

	<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses" /></h2>
	
	<p>
		<bean:define id="periodSemester" name="bolonhaStudentEnrollmentBean" property="executionPeriod.semester" />
		<bean:define id="executionYearName" name="bolonhaStudentEnrollmentBean" property="executionPeriod.executionYear.year" />
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.executionPeriod"/>:</strong> <bean:message bundle="STUDENT_RESOURCES"  key="label.periodDescription" arg0="<%=periodSemester.toString()%>" arg1="<%=executionYearName.toString()%>" /><br/>
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.registration.basic"/>:</strong> <bean:write name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.degreeCurricularPlan.presentationName"/> 
	</p>
	
	<ul>
		<li>
			<html:link action="/bolonhaStudentEnrollment.do?method=showEnrollmentInstructions" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewInstructions"/></html:link>
		</li>
		<li>
			<bean:define id="studentCurricularPlan" name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan" />
			<bean:define id="degreeId" name="studentCurricularPlan" property="degree.idInternal" />
			<bean:define id="degreeCurricularPlanId" name="studentCurricularPlan" property="degreeCurricularPlan.idInternal" />
			<bean:define id="executionPeriodId" name="bolonhaStudentEnrollmentBean" property="executionPeriod.idInternal" />
			
			<html:link href="<%=request.getContextPath() + "/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?organizeBy=groups&amp;showRules=false&amp;hideCourses=false&amp;degreeID=" + degreeId + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanId + "&amp;executionPeriodOID=" + executionPeriodId%>" target="_blank">
				<bean:message bundle="STUDENT_RESOURCES"  key="label.viewDegreeCurricularPlan"/>
			</html:link>
		</li>
		<li>
			<html:link action="/viewCurriculum.do?method=prepare" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewStudentCurricularPlan"/></html:link>
		</li>
		
	
	</ul>


	<logic:messagesPresent message="true" property="<%= ActionMessages.GLOBAL_MESSAGE %>">
		<ul class="mtop15 mbottom1 nobullet list2">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<logic:messagesPresent message="true" property="enrol">
		<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.errors.in.enrolment" />:
		<p class="mtop15">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<span class="success0"><bean:write name="messages" /></span>
			</html:messages>
		</p>
	</logic:messagesPresent>
	
	
	<logic:messagesPresent message="true" property="unenrol">
		<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.errors.in.unEnrolment" />
		<p class="mtop15">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<span class="success0"><bean:write name="messages" /></span>
			</html:messages>
		</p>
	</logic:messagesPresent>
	
	<fr:form action="/bolonhaStudentEnrollment.do">
		<input type="hidden" name="method" />
		
		<p class="mtop15 mbottom025">
			<bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:
		</p>
		<p class="mtop025 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
		</p>
		
		
		<fr:edit id="bolonhaStudentEnrolments" name="bolonhaStudentEnrollmentBean">
			<fr:layout name="bolonha-student-enrolment">
				<fr:property name="enrolmentClasses" value="enrolled smalltxt,enrolled smalltxt aright,enrolled smalltxt aright,enrolled smalltxt aright,enrolled aright" />
				<fr:property name="curricularCourseToEnrolClasses" value="smalltxt, smalltxt aright, smalltxt aright, aright" />				
				<fr:property name="groupRowClasses" value="bgcolor6" />
				
			</fr:layout>
		</fr:edit>
		
		
		<p class="mtop15 mbottom05"><bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:</p>
		<p class="mtop05 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
		</p>
	
	</fr:form>
	
</logic:present>
