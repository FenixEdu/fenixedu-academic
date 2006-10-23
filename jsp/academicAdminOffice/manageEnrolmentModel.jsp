<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h2><strong><bean:message key="link.student.manageEnrolmentModel"
	bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>

<fr:edit name="enrolmentModelBean" schema="student.manageEnrolmentModel">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:edit>
<br/>
<html:link page="/student.do?method=visualizeRegistration"
	paramId="registrationID" paramName="enrolmentModelBean"
	paramProperty="registration.idInternal">
	<bean:message key="link.student.back"
		bundle="ACADEMIC_OFFICE_RESOURCES" />
</html:link>
