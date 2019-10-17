<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key="title.teachers"/></h2>


<bean:define id="isResponsible" name="professorship" property="responsibleFor" />

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>	
</p>


<table class="tstyle2 tdcenter">
	<tr>
		<th><bean:message key="label.username" bundle="APPLICATION_RESOURCES" /></th>
		<th><bean:message key="label.name" /></th>
		<th><bean:message key="label.teacher.responsible" /></th>			
	</tr>	
	<logic:iterate id="professorship" name="executionCourse" property="professorships">
	<bean:define id="person" name="professorship" property="person" />
	<tr>
		<td><bean:write name="person"  property="username" /></td>
		<td><bean:write name="person" property="name" /></td>	
		<logic:equal name="professorship" property="responsibleFor" value="false">
			<td>
				<bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES" />
			</td>
		</logic:equal>
		<logic:equal name="professorship" property="responsibleFor" value="true">
			<td>
				<bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES" />
			</td>
		</logic:equal>
	</tr>
	</logic:iterate>	
</table>
