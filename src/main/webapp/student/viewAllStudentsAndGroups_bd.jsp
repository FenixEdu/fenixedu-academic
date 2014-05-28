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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>

<logic:present name="infoSiteStudentsAndGroups">

<span class="error0"><!-- Error messages go here --><html:errors /></span>

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<h2><bean:message key="title.viewAllStudentsAndGroups"/></h2>
		<p><span class="warning0"><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></span></p>
		<ul>
			<li>
			<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
	   		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
	   		</li>
		</ul>
	</logic:empty>
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<h2><bean:message key="title.viewAllStudentsAndGroups"/></h2>
		
		<ul>
			<li><html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
	   		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
	   		</li>
		</ul>
	
	<p><strong>Agrupamento:</strong> <span class="infoop4"><bean:write name="infoSiteStudentsAndGroups" property="infoGrouping.name"/></span></p>

 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
	<bean:message key="label.student.NumberOfStudents" /><%= count %>
	
<table class="tstyle4" width="75%" cellpadding="0" border="0">
	<tbody>

	<tr>
		<th width="10%"><bean:message key="label.studentGroupNumber" />
		</th>
		<th width="16%"><bean:message key="label.numberWord" />
		</th>
		<th width="53%"><bean:message key="label.nameWord" />
		</th>
		<th width="26%"><bean:message key="label.emailWord" />
		</th>
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>
		<bean:define id="username" name="LOGGED_USER_ATTRIBUTE" property="username" type="java.lang.String"/>
		<logic:equal name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr class="highlight">
				<td>
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
				<td>
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>
				<td>
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>
				<td>
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:equal>
		<logic:notEqual name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr>
				<td>
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>	
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:notEqual>
	 </logic:iterate>

</tbody>
</table>

<br/>


  </logic:notEmpty>

	 
</logic:present>
