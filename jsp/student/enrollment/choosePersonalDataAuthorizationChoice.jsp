<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
	<h2><bean:message key="label.enrollment.courses" bundle="STUDENT_RESOURCES" /></h2>

	<fr:form
		action="/studentEnrollmentManagement.do?method=choosePersonalDataAuthorizationChoice">
		
		<fr:edit name="student" schema="Student.editPersonalDataAuthorizationChoiceForCurrentExecutionYear">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
			</fr:layout>
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES"  key="label.continue"/></html:submit>
	</fr:form>

</logic:present>

