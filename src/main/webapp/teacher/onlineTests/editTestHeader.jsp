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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<h2><bean:message key="title.editTestHeader" /></h2>

<login:present name="test" />

<html:form action="/testEdition">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editTestHeader" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
	<bean:define id="testCode" name="test" property="externalId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=testCode.toString()%>" />
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.editTestHeader.information" /></td>
		</tr>
	</table>
	<br />
	<br />
	<table>
		<tr>
			<td><bean:message key="label.test.title" /></td>
		</tr>
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" size="50" name="test" property="title" /></td>
			<td><span class="error"><!-- Error messages go here --><html:errors /></span></td>
			<tr />
			<tr>
				<td><bean:message key="label.test.information" /></td>
			</tr>
			<tr>
				<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.information" rows="7" cols="45" name="test" property="information" /></td>
				<tr />
	</table>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.change" />
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear" />
	</html:reset>
</html:form>
