<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.student.curriculum" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrations" schema="student.registrationsToList" >
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="startDate=desc"/>	
		<fr:property name="classes" value="tstyle1 thlight mtop025"/>
		<fr:property name="columnClasses" value="acenter,acenter,,,acenter,"/>
		<fr:property name="linkFormat(view)" value="/studentTutorship.do?method=showStudentRegistration&amp;studentNumber=${student.number}&amp;registrationOID=${idInternal}" />
		<fr:property name="key(view)" value="view.curricular.plans"/>
		<fr:property name="bundle(view)" value="STUDENT_RESOURCES"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>

