<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><strong><bean:message key="link.studentOperations.viewStudents" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

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
		<fr:view name="students" schema="student.searchResult">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="linkFormat(view)" value="/student.do?method=visualizeStudent&studentID=${idInternal}" />
				<fr:property name="key(view)" value="link.student.visualizeStudent"/>
				<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(view)" value="true"/>	
			</fr:layout>
		</fr:view>
	</logic:greaterThan>
</logic:present>


