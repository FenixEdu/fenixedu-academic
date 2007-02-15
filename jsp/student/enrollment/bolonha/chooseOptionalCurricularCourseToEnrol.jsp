<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><strong><bean:message key="label.enrollment.optional.course"
	bundle="STUDENT_RESOURCES" /></strong></h2>
<br />

<fr:form action="/bolonhaStudentEnrollment.do">
	<input type="hidden" name="method" value="" />
	<fr:edit id="optionalEnrolment" name="optionalEnrolmentBean"
		schema="BolonhaStudentOptionalEnrollmentBean">
		<fr:destination name="updateComboBoxes"
			path="/bolonhaStudentEnrollment.do?method=updateParametersToSearchOptionalCurricularCourses" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value="listClasses,," />
		</fr:layout>
	</fr:edit>

	<br />
	
	<logic:messagesPresent message="true">
		<ul class="mtop15 mbottom1 nobullet list2">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	
	<br />
	<br />

	<logic:present name="optionalEnrolmentBean" property="degreeCurricularPlan">
		<fr:edit id="degreeCurricularPlan" name="optionalEnrolmentBean">
			<fr:layout name="bolonha-student-optional-enrolments">
				<fr:property name="groupRowClasses" value="se_groups" />
			</fr:layout>
		</fr:edit>
		<br />
		<br />
	</logic:present>



</fr:form>

<bean:define id="studentCurricularPlanId" name="optionalEnrolmentBean" property="studentCurricularPlan.idInternal" />
<fr:form action="<%="/bolonhaStudentEnrollment.do?method=cancelChooseOptionalCurricularCourseToEnrol&amp;studentCurricularPlanId=" + studentCurricularPlanId %>">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/>
	</html:submit>
</fr:form>

