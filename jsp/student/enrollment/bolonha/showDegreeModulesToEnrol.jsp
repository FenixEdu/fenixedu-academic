<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<logic:present role="STUDENT">

	<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
	<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses" /></h2>


	<bean:define id="periodSemester" name="bolonhaStudentEnrollmentBean" property="executionPeriod.semester" />
	<bean:define id="executionYearName" name="bolonhaStudentEnrollmentBean" property="executionPeriod.executionYear.year" />



	<p class="mtop15 mbottom025">
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.executionPeriod"/>:</strong> <bean:message bundle="STUDENT_RESOURCES"  key="label.periodDescription" arg0="<%=periodSemester.toString()%>" arg1="<%=executionYearName.toString()%>" />
	</p>
	<p class="mtop0 mbottom15">
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.registration.basic"/>:</strong> <bean:write name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.degreeCurricularPlan.presentationName"/> 
	</p>

	
	
	<ul class="mbottom2">
		<li>
			<html:link action="/bolonhaStudentEnrollment.do?method=showEnrollmentInstructions" styleClass="externallink" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewInstructions"/></html:link>
		</li>
		<li>
			<bean:define id="studentCurricularPlan" name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan" />
			<bean:define id="degreeId" name="studentCurricularPlan" property="degree.idInternal" />
			<bean:define id="degreeCurricularPlanId" name="studentCurricularPlan" property="degreeCurricularPlan.idInternal" />
			<bean:define id="executionPeriodId" name="bolonhaStudentEnrollmentBean" property="executionPeriod.idInternal" />
			
			<html:link href="<%=request.getContextPath() + "/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?organizeBy=groups&amp;showRules=false&amp;hideCourses=false&amp;degreeID=" + degreeId + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanId + "&amp;executionPeriodOID=" + executionPeriodId%>" styleClass="externallink" target="_blank">
				<bean:message bundle="STUDENT_RESOURCES"  key="label.viewDegreeCurricularPlan"/>
			</html:link>
		</li>
		<li>
			<html:link action="/viewCurriculum.do?method=prepare" styleClass="externallink" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewStudentCurricularPlan"/></html:link>
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
		<div class="error0" style="padding: 0.5em;">
		<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.errors.in.enrolment" />:</strong></p>
		<ul class="mvert05">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		</div>
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
				<fr:property name="enrolmentClasses" value="se_enrolled smalltxt,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled aright" />
				<fr:property name="temporaryEnrolmentClasses" value="se_temporary smalltxt,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary aright" />
				<fr:property name="impossibleEnrolmentClasses" value="se_impossible smalltxt,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible aright" />
				<fr:property name="curricularCourseToEnrolClasses" value="smalltxt, smalltxt aright, smalltxt aright, aright" />				
				<fr:property name="groupRowClasses" value="se_groups" />
			</fr:layout>
		</fr:edit>
		
		
		<p class="mtop15 mbottom05"><bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:</p>
		<p class="mtop05 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
		</p>
	
	</fr:form>


<p class="mtop15"><em><bean:message bundle="STUDENT_RESOURCES"  key="message.enrollment.terminated"/> <html:link action="/viewCurriculum.do?method=prepare"><bean:message bundle="STUDENT_RESOURCES"  key="message.student.curriculum"/></html:link>.</em></p>

</logic:present>