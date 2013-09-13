<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.student.curriculum" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrations" schema="student.registrationsToList" >
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="startDate=desc"/>	
		<fr:property name="classes" value="tstyle1 thlight mtop025"/>
		<fr:property name="columnClasses" value="acenter,acenter,,,acenter,"/>
		<fr:property name="linkFormat(view)" value="/studentTutorship.do?method=showStudentRegistration&amp;studentNumber=${student.number}&amp;registrationOID=${externalId}" />
		<fr:property name="key(view)" value="view.curricular.plans"/>
		<fr:property name="bundle(view)" value="STUDENT_RESOURCES"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>

