<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
	<h2>
		<bean:write name="bolonhaStudentEnrollmentBean"  property="funcionalityTitle" />
	</h2>

	<bean:define id="periodSemester" name="bolonhaStudentEnrollmentBean" property="executionPeriod.semester" />
	<bean:define id="executionYearName" name="bolonhaStudentEnrollmentBean" property="executionPeriod.executionYear.year" />



	<p class="mtop15 mbottom025">
		<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.executionPeriod"/>:</strong> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.periodDescription" arg0="<%=periodSemester.toString()%>" arg1="<%=executionYearName.toString()%>" />
	</p>
	<p class="mtop0 mbottom15">
		<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.registration"/>:</strong> <bean:write name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.degreeCurricularPlan.presentationName"/> 
	</p>

	
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
	
	<bean:define id="action" name="action"/>
	<html:form action="<%= action.toString() %>">
		<html:hidden property="method" value=""/>
		<html:hidden property="withRules"/>

		
		<p class="mtop15 mbottom025">
			<bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:
		</p>
		<p class="mtop025 mbottom1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='backToStudentEnrollments';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.back"/></html:submit>			
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
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='backToStudentEnrollments';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.back"/></html:submit>
		</p>
	
	</html:form>



<p class="mtop2 mbottom0"><em><bean:message bundle="APPLICATION_RESOURCES"  key="label.legend"/>:</em></p>
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