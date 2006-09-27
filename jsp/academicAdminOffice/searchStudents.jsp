<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="link.students" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<fr:edit name="studentsSearchBean" schema="net.sourceforge.fenixedu.domain.student.StudentsSearchBean" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>

<br/>

<logic:present name="students">
	<bean:size id="numberStudents" name="students"/>
	
	<logic:equal name="numberStudents" value="0">
		<logic:present name="studentsSearchBean" property="number">
			<bean:message key="message.no.students.found" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</logic:present>
	</logic:equal>
	
	<logic:greaterThan name="numberStudents" value="1">
		<fr:view name="students" schema="net.sourceforge.fenixedu.domain.student.StudentsSearchBean.searchResult">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:view>
	</logic:greaterThan>
</logic:present>


