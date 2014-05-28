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
<logic:present name="infoStudentTestQuestionList">
<center>
<logic:empty name="infoStudentTestQuestionList">
	<h2><bean:message key="message.test.no.available"/></h2>
</logic:empty>
	
<logic:notEmpty name="infoStudentTestQuestionList" >
	
	<logic:present name="successfulChanged">
		<span class="error"><!-- Error messages go here --><bean:message key="message.successfulChanged"/></span>
		<br/>
		<table>
		<logic:iterate id="changed" name="successfulChanged">
		<tr><td><bean:write name="changed" property="label"/></td>
		<td><bean:write name="changed" property="value"/></td></tr>
		</logic:iterate>
		</table>
	</logic:present>
	<logic:present name="insuccessfulAdvisoryDistribution">
		<span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistributionForAll"/></span>
	</logic:present>
	<logic:present name="infoStudentList">
		<bean:size id="infoStudentListSize" name="infoStudentList"/>
		<logic:notEqual name="infoStudentListSize" value="0">
			<table><tr><td><span class="error"><!-- Error messages go here --><bean:message key="message.insuccessfulAdvisoryDistribution"/></span></td></tr>
			<logic:iterate id="student" name="infoStudentList">
				<tr><td><span class="error"><!-- Error messages go here --><bean:write name="student" property="number"/></span></td></tr>
			</logic:iterate>
			</table>
		</logic:notEqual>
	</logic:present>
	
	<html:form action="/studentTestManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="simulateTest"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.doTestSimulation" property="doTestSimulation" value="true"/>
	
	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
	<bean:define id="distributedTestCode" name="distributedTest" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testInformation" property="testInformation" name="distributedTest" property="testInformation"/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testType" property="testType" value="<%=testType.toString()%>"/>
	<bean:define id="availableCorrection" name="distributedTest" property="correctionAvailability.availability"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.availableCorrection" property="availableCorrection" value="<%=availableCorrection.toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.imsFeedback" property="imsFeedback" name="distributedTest" property="imsFeedback"/>
	
	<bean:define id="objectCode" name="distributedTest" property="infoTestScope.infoObject.externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%= distributedTestCode.toString() %>"/>
	<h2><bean:write name="distributedTest" property="title"/></h2>
	<b><bean:write name="distributedTest" property="testInformation"/></b>
	</center>


	<jsp:include page="showStudentTest.jsp">
		<jsp:param name="pageType" value="correction"/>
		<jsp:param name="correctionType" value="cla"/>
		<jsp:param name="testCode" value="<%=distributedTestCode%>"/>
 	</jsp:include>
	<br/>
	<br/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	
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
<logic:notPresent name="infoStudentTestQuestionList">
<center>
	<h2><bean:message key="message.test.no.available"/></h2>
</center>
</logic:notPresent>