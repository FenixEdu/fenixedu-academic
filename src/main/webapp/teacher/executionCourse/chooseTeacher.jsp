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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:equal name="loggedIsResponsible" value="true">
	<h3 class="mbottom0"><bean:message key="label.teacher"/></h3>											
	<table class="tstyle5">	
		<logic:notEmpty name="summariesManagementBean" property="professorship">
			<bean:define id="professorship" name="summariesManagementBean" property="professorship" />
		 	<bean:define id="professorshipId" name="summariesManagementBean" property="professorship.externalId" />
			<tr>
		 		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="<%= professorshipId.toString()%>"/></td>				
		 		<td><bean:write name="professorship" property="person.name"/></td>
	 		</tr>
		</logic:notEmpty>
		<logic:empty name="summariesManagementBean" property="professorship">
			<bean:define id="professorship" name="summariesManagementBean" property="professorshipLogged" />
		 	<bean:define id="professorshipId" name="summariesManagementBean" property="professorshipLogged.externalId" />
			<tr>
		 		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="<%= professorshipId.toString()%>"/></td>				
		 		<td><bean:write name="professorship" property="person.name"/></td>
	 		</tr>
		</logic:empty>											
		<tr>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="0" /></td>
			<td>
				<bean:message key="label.teacher.in" />
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" name="summariesManagementForm" property="teacherId" size="4" />
				(<bean:message key="label.number" />)
			</td>
		</tr>
		<tr>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="-1" /></td>
			<td>
				<bean:message key="label.teacher.out" />
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherName" name="summariesManagementForm" property="teacherName" size="40"/>
				(<bean:message key="label.name" />)
			</td>
		</tr>				
	</table>			
</logic:equal>