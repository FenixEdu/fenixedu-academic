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

<html:form action="/testChecksumValidation">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="validateTestChecksum"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
	<bean:define id="studentCode" name="registration" property="externalId"/>
	<bean:define id="distributedTestCode" name="distributedTest" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%=studentCode.toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=distributedTestCode.toString()%>"/>

	<table>
		<tr>
			<td><b><bean:message key="label.markSheet.name"/></b></td>
			<td><bean:write name="registration" property="student.person.name"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="label.markSheet.number"/></b></td>
			<td><bean:write name="registration" property="number"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="lable.test"/></b></td>
			<td><bean:write name="distributedTest" property="title"/>
			(<bean:write name="distributedTest" property="externalId"/>)</td>
		</tr>
		<tr>
			<td><b><bean:message key="label.date"/><b><bean:message key="message.dateTimeFormat"/></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.dayFormatted" maxlength="19" size="19" property="date" />
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="button.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit></td>
		</tr>	
		<logic:present name="checksum">
			<tr>
				<td><b><bean:message key="label.validationCode"/></b></td>
				<td><bean:write name="checksum"/></td>
			</tr>	
		</logic:present>
	</table>
</html:form>

