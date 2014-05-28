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

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	
	<logic:empty name="component" property="infoSiteStudentsAndGroupsList">
	<h2><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></h2>
	</logic:empty> 

	<logic:notEmpty name="component" property="infoSiteStudentsAndGroupsList">

	<logic:equal name="ShiftChosenType" value="1">
	<h2><bean:message key="title.viewStudentsAndGroupsByShift"/></h2>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="2">
	<h2><bean:message key="title.viewStudentsAndGroupsWithoutShift"/></h2>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="3">
	<h2><bean:message key="title.viewAllStudentsAndGroups"/></h2>
	</logic:equal>
	
	
 	<bean:size id="count" name="component" property="infoSiteStudentsAndGroupsList"/>
 	
 	<logic:equal name="ShiftChosenType" value="1">
	<bean:message key="label.teacher.NumberOfStudentsInShift" /><%= count %>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="2">
	<bean:message key="label.teacher.NumberOfStudentsWithoutShift" /><%= count %>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="3">
	<bean:message key="label.teacher.NumberOfStudents" /><%= count %>
	</logic:equal>
	
	<br/>	
	<br/>
	
	<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	
	<tr>
		<th><bean:message key="label.studentGroupNumber" /></th>

		<th><bean:message key="label.number.abbr" /></th>
		
		<th><bean:message key="label.nameWord" /></th>
		
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="component" property="infoSiteStudentsAndGroupsList">
	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>
		
		<tr>		
			<td><bean:write name="infoStudentGroup" property="groupNumber"/></td>
			
			<td><bean:write name="infoSiteStudentInformation" property="number"/></td>	
			
			<td><bean:write name="infoSiteStudentInformation" property="name"/></td>		
			
		</tr>				
	 </logic:iterate>

</table>

<br/>
<br/>
	 
</logic:notEmpty> 

</logic:present>

<logic:notPresent name="siteView" property="component">
<h2><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></h2>
</logic:notPresent>