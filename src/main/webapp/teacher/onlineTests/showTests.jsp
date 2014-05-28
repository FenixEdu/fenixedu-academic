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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<h2><bean:message key="title.showTests" /></h2>

<logic:present name="testList">
	<bean:define id="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />

	<bean:size id="testsSize" name="testList" />
	<logic:equal name="testsSize" value="0">
		<span class="error"><!-- Error messages go here --><bean:message key="message.tests.no.tests" /></span>
	</logic:equal>
	<logic:notEqual name="testsSize" value="0">
		<table>
			<tr>
				<td class="infoop"><bean:message key="message.showTests.information" /></td>
			</tr>
		</table>
		<br />
		<div class="gen-button"><html:link
			page="<%= "/testsManagement.do?method=prepareCreateTest&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")%>">
			<bean:message key="link.createTest" />
		</html:link></div>
		<br />
		<span class="error"><!-- Error messages go here --><html:errors property="InvalidDistribution" /></span>
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.test.title" /></th>
				<th class="listClasses-header"><bean:message key="label.test.creationDate" /></th>
				<th class="listClasses-header"><bean:message key="label.test.lastModifiedDate" /></th>
				<th width="100" class="listClasses-header"><bean:message key="label.test.numberOfQuestions" /></th>
			</tr>
			<logic:iterate id="test" name="testList" type="net.sourceforge.fenixedu.domain.onlineTests.Test">
				<tr>
					<td class="listClasses"><bean:write name="test" property="title" /></td>
					<td class="listClasses"><bean:write name="test" property="creationDateFormatted" /></td>
					<td class="listClasses"><bean:write name="test" property="lastModifiedDateFormatted" /></td>
					<bean:size id="numberOfQuestions" name="test" property="testQuestions"/>
					<td class="listClasses"><bean:write name="numberOfQuestions" /></td>
					<bean:define id="testCode" name="test" property="externalId" />
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testEdition.do?method=editTest&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;testCode=" + testCode %>">
						<bean:message key="label.edit" />
					</html:link></div>
					</td>
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testsManagement.do?method=prepareDeleteTest&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;testCode=" + testCode %>">
						<bean:message key="link.remove" />
					</html:link></div>
					</td>
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testEdition.do?method=editAsNewTest&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;testCode=" + testCode %>">
						<bean:message key="label.duplicate" />
					</html:link></div>
					</td>
					<td>
					<div class="gen-button"><html:link
						page="<%= "/testDistribution.do?method=prepareDistributeTest&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;testCode=" + testCode %>">
						<bean:message key="link.student.room.distribution" />
					</html:link></div>
					</td>
					<%--
					<td>
					<div class="gen-button"><html:link
						page="<%= "/studentTestManagement.do?method=chooseTestSimulationOptions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;testCode=" +testCode %>">
						<bean:message key="label.Simulate" />
					</html:link></div>
					</td>
					--%>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>

</logic:present>
