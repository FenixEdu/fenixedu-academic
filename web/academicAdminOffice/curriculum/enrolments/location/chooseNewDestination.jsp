<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	<h2><strong><bean:message key="label.optionalCurricularCourses.move" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>

	<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="idInternal" />
	<bean:define id="url">/optionalCurricularCoursesLocation.do?scpID=<bean:write name="studentCurricularPlan" property="idInternal" /></bean:define>
	
	<fr:form action="<%= url.toString() %>">
		<input type="hidden" name="method" />
		
		<logic:messagesPresent message="true">
			<ul class="nobullet list6">
				<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
					<li><span class="error0"><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
			<br/>
		</logic:messagesPresent>

		<fr:edit id="optionalCurricularCoursesLocationBean" name="optionalCurricularCoursesLocationBean" visible="false" />
		
		<logic:notEmpty name="optionalCurricularCoursesLocationBean" property="enrolmentBeans">
		<strong><bean:message key="label.optionalCurricularCourse.enrolments" bundle="APPLICATION_RESOURCES" />:</strong>
		<fr:edit id="optionalCurricularCoursesLocationBean-enrolmentBeans" name="optionalCurricularCoursesLocationBean" property="enrolmentBeans"
				 schema="OptionalCurricularCoursesLocationBean.edit.enrolmentBeans">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight mtop05"/>
				<fr:property name="columnClasses" value="acenter,,"/>
			</fr:layout>
			<fr:destination name="invalid" path="/optionalCurricularCoursesLocation.do?method=chooseNewDestinationInvalid" />
		</fr:edit>
		</logic:notEmpty>
		<logic:notEmpty name="optionalCurricularCoursesLocationBean" property="optionalEnrolmentBeans">
		<strong><bean:message key="label.optionalCurricularCourse.optionalEnrolments" bundle="APPLICATION_RESOURCES" />:</strong>
		<fr:edit id="optionalCurricularCoursesLocationBean-optionalEnrolmentBeans" name="optionalCurricularCoursesLocationBean" property="optionalEnrolmentBeans"
				 schema="OptionalCurricularCoursesLocationBean.edit.optionalEnrolmentBeans">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight mtop05"/>
				<fr:property name="columnClasses" value="acenter,,"/>
			</fr:layout>
			<fr:destination name="invalid" path="/optionalCurricularCoursesLocation.do?method=chooseNewDestinationInvalid" />
		</fr:edit>
		</logic:notEmpty>
		
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='moveEnrolments';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.move" />
			</html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='prepare';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
			</html:cancel>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='backToStudentEnrolments';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel" />
			</html:cancel>
		</p>

	</fr:form>
</logic:present>
