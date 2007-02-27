<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.academicAdminOffice" /></em>
<h2>
	<bean:message key="label.student.enrollment.optional.course" bundle="ACADEMIC_OFFICE_RESOURCES" />
	<bean:define id="withRules" name="bolonhaStudentEnrollmentForm" property="withRules" />
	<logic:equal name="withRules" value="true">
		(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.student.enrollment.withRules"/>)
	</logic:equal>
	<logic:equal name="withRules" value="false">
		(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.student.enrollment.withoutRules"/>)
	</logic:equal>
</h2>

<html:form action="/bolonhaStudentEnrollment.do">

	<html:hidden property="method" value=""/>
	<html:hidden property="withRules"/>

	<fr:context>
		<fr:edit id="optionalEnrolment" name="optionalEnrolmentBean"
			schema="BolonhaStudentOptionalEnrollmentBean.chooseCriteria">
			<fr:destination name="updateComboBoxes"
				path="/bolonhaStudentEnrollment.do?method=updateParametersToSearchOptionalCurricularCourses" />
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
	
	
		<logic:messagesPresent message="true" property="error">
			<div class="mtop1 mbottom15 error0" style="padding: 0.5em;">
			<p class="mvert0"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.enrollment.errors.in.enrolment" />:</strong></p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
					<li><span class="error0"><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
			</div>
		</logic:messagesPresent>
	
	
		<logic:present name="optionalEnrolmentBean" property="degreeCurricularPlan">
			<fr:edit id="degreeCurricularPlan" name="optionalEnrolmentBean">
				<fr:layout name="bolonha-student-optional-enrolments">
					<fr:property name="classes" value="mtop15" />
					<fr:property name="groupRowClasses" value="se_groups" />
				</fr:layout>
			</fr:edit>
		</logic:present>
		
	</fr:context>

</html:form>

<bean:define id="studentCurricularPlanId" name="optionalEnrolmentBean" property="studentCurricularPlan.idInternal" />


<html:form action="<%= "/bolonhaStudentEnrollment.do?method=cancelChooseOptionalCurricularCourseToEnrol&amp;withRules=" + withRules.toString() %>">
<fr:context>
	<fr:edit id="optionalEnrolment" name="optionalEnrolmentBean" visible="false"/>
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/>
		</html:submit>
	</p>
</fr:context>
</html:form>

