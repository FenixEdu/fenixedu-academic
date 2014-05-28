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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<h2><bean:message key="label.shifts"/></h2>

<logic:notEmpty name="shifts">
	<table class="tstyle1 thlight tdcenter">
		<tr>
			<th><bean:message key="label.shift"/></th>
			<th><bean:message key="property.shift.capacity"/></th>
			<th><bean:message key="property.number.students.attending.course"/></th>
			<th></th>
		</tr>
		<logic:iterate id="shift" name="shifts">
		<tr>
			<td style="text-align:left"><fr:view name="shift" property="presentationName"/></td>
			<td><fr:view name="shift" property="lotacao"/></td>
			<td><fr:view name="shift" property="studentsCount"/></td>
			<bean:define id="executionCourseID" name="executionCourseID"></bean:define>
			<td><html:link page="<%= "/manageExecutionCourse.do?method=editShift&executionCourseID=" + executionCourseID %>" paramId="shiftID" paramName="shift" paramProperty="externalId"><bean:message key="label.edit"/></html:link></td>
		</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="shifts">
	<p><em><bean:message key="label.shifts.nondefined"/></em></p>
</logic:empty>
