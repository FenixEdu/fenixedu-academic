<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="label.internationalrelations.students.search.title"
	bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>

<fr:edit name="studentsSearchBean" schema="student.StudentsSearchBean" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle5 thmiddle thright thlight"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>


<logic:present name="students">
	<bean:size id="numberStudents" name="students"/>
	
	<logic:equal name="numberStudents" value="0">
		<logic:present name="studentsSearchBean" property="number">
			<p>
				<em><bean:message key="message.no.students.found" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
			</p>
		</logic:present>
	</logic:equal>
	
	<logic:greaterThan name="numberStudents" value="1">
		<fr:view name="students" schema="Student.view-with-number-and-name">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="linkFormat(view)" value="/students.do?method=visualizeStudent&studentID=${externalId}" />
				<fr:property name="key(view)" value="link.student.visualizeStudent"/>
				<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(view)" value="true"/>	
			</fr:layout>
		</fr:view>
	</logic:greaterThan>
</logic:present>


