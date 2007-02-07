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
	<br />
	<br />

	<logic:present name="optionalEnrolmentBean" property="degreeCurricularPlan">
		<fr:edit id="degreeCurricularPlan" name="optionalEnrolmentBean">
			<fr:layout name="bolonha-student-optional-enrolments">
				<fr:property name="linkFormat"
					value="/studentOptionalEnrolments.do?method=enrol&scpID=${studentCurricularPlan.idInternal}&executionPeriodID=${executionPeriod.idInternal}&curriculumGroupID=${curriculumGroup.idInternal}&contextID=${contex.idInternal}&degreeType=${degreeType}&degreeID=${degree.idInternal}&dcpID=${degreeCurricularPlan.idInternal}" />
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

