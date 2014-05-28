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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>	
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);
%>
<bean:message key="message.onlineTest.info"/>
<br/><br/>
<logic:present name="studentTestQuestionList">
	<center>
<logic:empty name="studentTestQuestionList">
	<h2><bean:message key="message.studentTest.no.available"/></h2>
</logic:empty>
	
<logic:notEmpty name="studentTestQuestionList" >
	
	<logic:iterate id="testQuestion" name="studentTestQuestionList" type="net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="externalId"/>
	<bean:define id="testScope" name="distributedTest" property="testScope"/>
	<bean:define id="domainObject" name="testScope" property="executionCourse"/>
	<bean:define id="objectCode" name="domainObject" property="externalId"/>
	<bean:define id="student" name="testQuestion" property="student"/>
	<bean:define id="studentCode" name="student" property="number"/>
	<bean:define id="registration" name="testQuestion" property="student.externalId"/>
	
	<html:form action="/studentTests">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doTest"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%= testCode.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%= studentCode.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.student" property="student" value="<%= registration.toString() %>"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
		<br/><br/>
		<b><bean:write name="date"/></b>
	</center>
	<jsp:include page="showStudentTest_bd.jsp">
		<jsp:param name="pageType" value="doTest" />
		<jsp:param name="testCode" value="<%=testCode%>"/>
 	</jsp:include>
	<table align="center">
		<tr class="infoop2"><td ><bean:message key="message.exportChecksumReportAfterSubmission"/></td></tr>
		<tr>
		<td class="forminline acenter"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="submit"><bean:message key="button.submitTest"/></html:submit>
			<%--
			<td><html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td>
			--%>
			</html:form>
			<html:form action="/studentTests">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="testsFirstPage"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%= testCode.toString() %>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.student" property="student" value="<%= registration.toString() %>"/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="button.back"/></html:submit></td></html:form>
		</tr>
	</table>	
	</logic:notEmpty>
</logic:present>
<center>
<logic:notPresent name="studentTestQuestionList">
<h2><bean:message key="message.studentTest.no.available"/></h2>
</logic:notPresent>
</center>