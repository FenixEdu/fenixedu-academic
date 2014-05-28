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
<logic:present name="studentTestQuestionList">
	<center><logic:empty name="studentTestQuestionList">
		<h2><bean:message key="message.test.no.available" /></h2>
	</logic:empty> <logic:notEmpty name="studentTestQuestionList">

		<logic:present name="successfulChanged">
			<span class="error"><!-- Error messages go here --><bean:message key="message.successfulChanged" /></span>
			<br />
			<table>
				<logic:iterate id="changed" name="successfulChanged">
					<logic:iterate id="student" name="changed" property="infoStudentList">
						<tr>
							<tr>
								<td><strong><bean:write name="changed" property="infoDistributedTest.title" /></strong></td>
								<td><bean:write name="student" property="number" /></td>
							</tr>
					</logic:iterate>
				</logic:iterate>
			</table>
		</logic:present>
		<logic:present name="insuccessfulAdvisoryDistribution">
			<span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistributionForAll" /></span>
		</logic:present>
		<logic:present name="studentWithoutAdvisory">
			<bean:size id="infoStudentListSize" name="studentWithoutAdvisory" />
			<logic:notEqual name="infoStudentListSize" value="0">
				<table>
					<tr>
						<td><span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistribution" /></span></td>
					</tr>
					<logic:iterate id="student" name="studentWithoutAdvisory">
						<tr>
							<td><span class="error"><!-- Error messages go here --><bean:write name="student" property="number" /></span></td>
						</tr>
					</logic:iterate>
				</table>
			</logic:notEqual>
		</logic:present>

		<html:form action="/studentTestManagement">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTestMarks" />

			<logic:iterate id="testQuestion" name="studentTestQuestionList"
				type="net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion" />
			<bean:define id="distributedTest" name="testQuestion" property="distributedTest"
				type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest" />
			<bean:define id="testCode" name="distributedTest" property="externalId" />

			<bean:define id="objectCode" name="distributedTest" property="testScope.executionCourse.externalId" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%= testCode.toString() %>" />

			<h2><bean:write name="distributedTest" property="title" /></h2>
			<b><bean:write name="distributedTest" property="testInformation" /></b></center>
	<br />
	<br />
	<bean:define id="testType" name="distributedTest" property="testType.type" />
	<%if(((Integer)testType).intValue()!=3){%>
	<b><bean:message key="label.test.totalClassification"/>:</b>&nbsp;<bean:write name="classification"/>
	<%}%>
	<jsp:include page="showStudentTest.jsp">
		<jsp:param name="pageType" value="correction"/>
		<jsp:param name="correctionType" value="studentCorrection"/>
		<jsp:param name="testCode" value="<%=testCode%>"/>
 	</jsp:include>

	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="studentTestQuestionList">
<center>
	<h2><bean:message key="message.test.no.available"/></h2>
</center>
</logic:notPresent>