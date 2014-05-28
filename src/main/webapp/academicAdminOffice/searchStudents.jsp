<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.studentOperations.viewStudents" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

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
				<fr:property name="classes" value="tstyle1 thlight mtop2"/>
	        	<fr:property name="columnClasses" value="acenter,,"/>
				<fr:property name="linkFormat(view)" value="/student.do?method=visualizeStudent&studentID=${externalId}" />
				<fr:property name="key(view)" value="link.student.visualizeStudent"/>
				<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(view)" value="true"/>	
			</fr:layout>
		</fr:view>
	</logic:greaterThan>
</logic:present>


