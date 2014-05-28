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
