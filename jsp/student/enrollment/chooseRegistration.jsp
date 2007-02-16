<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h2><bean:message key="title.student.shift.enrollment" bundle="STUDENT_RESOURCES" /></h2>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrationsToEnrolByStudent" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight mtop025 asdasd"/>
		<fr:property name="columnClasses" value=",tdhl1,,"/>
		<fr:property name="linkFormat(enrol)" value="/studentShiftEnrollmentManager.do?method=prepareStartViewWarning&registrationOID=${idInternal}" />
		<fr:property name="key(enrol)" value="enrol.in.shift"/>
		<fr:property name="bundle(enrol)" value="STUDENT_RESOURCES"/>
		<fr:property name="contextRelative(enrol)" value="true"/>
	</fr:layout>
</fr:view>

