<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>	
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:notEmpty name="registrations">
	<fr:view name="registrations" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop025"/>
			<fr:property name="linkFormat(view)" value="/viewEnroledExecutionCourses.do?method=select&registrationId=${externalId}" />
			<fr:property name="key(view)" value="link.grouping.chooseGroups"/>
			<fr:property name="bundle(view)" value="STUDENT_RESOURCES"/>
			<fr:property name="contextRelative(view)" value="true"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="registrations">
	<bean:message key="label.noStudentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</logic:empty>
