<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:notEmpty name="registrations">
	<fr:view name="registrations" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop025 asdasd"/>
			<fr:property name="columnClasses" value=",tdhl1,,"/>
			<fr:property name="linkFormat(view)" value="/viewEnroledExecutionCourses.do?method=select&registrationId=${idInternal}" />
			<fr:property name="key(view)" value="link.grouping.chooseGroups"/>
			<fr:property name="bundle(view)" value="STUDENT_RESOURCES"/>
			<fr:property name="contextRelative(view)" value="true"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="registrations">
	<bean:message key="label.noStudentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</logic:empty>
