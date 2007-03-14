<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.title.seniorInfo" bundle="STUDENT_RESOURCES" /></h2>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrations" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight mtop025 asdasd"/>
		<fr:property name="columnClasses" value=",tdhl1,,"/>
		<fr:property name="linkFormat(view)" value="/seniorInformation.do?method=prepare&registrationOID=${idInternal}" />
		<fr:property name="key(view)" value="view.senior.info"/>
		<fr:property name="visibleIf(view)" value="degreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree"/>
		<fr:property name="bundle(view)" value="STUDENT_RESOURCES"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>
