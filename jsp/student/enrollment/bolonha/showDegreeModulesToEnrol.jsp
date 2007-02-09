<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">

	<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses" /></h2>
	
	<p>
		<bean:define id="periodSemester" name="bolonhaStudentEnrollmentBean" property="executionPeriod.semester" />
		<bean:define id="executionYearName" name="bolonhaStudentEnrollmentBean" property="executionPeriod.executionYear.year" />
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.executionPeriod"/></strong>: <bean:message bundle="STUDENT_RESOURCES"  key="label.periodDescription" arg0="<%=periodSemester.toString()%>" arg1="<%=executionYearName.toString()%>" /><br/>
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.studentCurricularPlan.basic"/>:</strong> <bean:write name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.degreeCurricularPlan.presentationName"/>		
	</p>
	
	<p>
		<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.firstTimeWarning"/> <html:link action="/bolonhaStudentEnrollment.do?method=showEnrollmentInstructions" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.instructions"/></html:link>
	</p>
	
	<logic:messagesPresent message="true">
		<ul class="nobullet">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
		
	
	<fr:form action="/bolonhaStudentEnrollment.do">
		<input type="hidden" name="method" />
			
		<fr:edit id="bolonhaStudentEnrolments" name="bolonhaStudentEnrollmentBean">
			<fr:layout name="bolonha-student-enrolment">
				<fr:property name="enrolmentClasses" value="smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright" />
				<fr:property name="curricularCourseToEnrolClasses" value="smalltxt, smalltxt aright, smalltxt aright, aright" />				
				<fr:property name="groupRowClasses" value="bgcolor2" />
				
			</fr:layout>
		</fr:edit>
		
		
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.update"/></html:submit>
		</p>
	
	</fr:form>
	
</logic:present>
