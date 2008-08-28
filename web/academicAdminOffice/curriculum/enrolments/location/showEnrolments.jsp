<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	<h2><strong><bean:message key="label.optionalCurricularCourses.move"	bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>

	<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="idInternal" />
	<fr:form action="<%="/optionalCurricularCoursesLocation.do?scpID=" + studentCurricularPlanId.toString() %>">
		<input type="hidden" name="method" />
		
		<logic:messagesPresent message="true">
			<ul class="nobullet list6">
				<html:messages id="messages" message="true"
					bundle="ACADEMIC_OFFICE_RESOURCES">
					<li><span class="error0"><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
			<br/>
		</logic:messagesPresent>

		<fr:view name="enrolments" schema="OptionalCurricularCoursesLocation.enrolment.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop05"/>
				<fr:property name="columnClasses" value="acenter width12em,, tderror1"/>
				
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="enrolmentsToChange" />
				<fr:property name="checkboxValue" value="idInternal" />	
			</fr:layout>
		</fr:view>
		
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='chooseNewDestination';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.continue" />
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='backToStudentEnrolments';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel" />
			</html:submit>
		</p>

	</fr:form>
</logic:present>
