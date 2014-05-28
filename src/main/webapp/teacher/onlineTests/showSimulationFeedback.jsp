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
<logic:present name="infoSiteStudentTestFeedback">
	<center><h2><bean:message key="message.studentTest.sent"/></h2></center>
	<bean:define id="responseNumber" name="infoSiteStudentTestFeedback" property="responseNumber"/>
	<bean:define id="notResponseNumber" name="infoSiteStudentTestFeedback" property="notResponseNumber"/>
	<bean:define id="errors" name="infoSiteStudentTestFeedback" property="errors"/>
	<table>
		<logic:iterate id="error" name="errors">
			<tr><td><span class="error"><!-- Error messages go here --><bean:write name="error"/></span></td></tr>
		</logic:iterate>
		<tr>
			<td><b><bean:message key="message.studentQuestionsAnsweredNumber"/></b></td>
			<td><bean:write name="responseNumber"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="message.studentQuestionsNotAnsweredNumber"/></b></td>
			<td><bean:write name="notResponseNumber"/></td>
		</tr>
	</table>
	
	
	
	<html:form action="/studentTestManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="simulateTest"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.doTestSimulation" property="doTestSimulation" value="true"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= request.getParameter("objectCode") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%= request.getParameter("distributedTestCode") %>"/>

	<logic:present name="infoStudentTestQuestionList">
		<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion"/>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
		<bean:define id="distributedTestCode" name="distributedTest" property="externalId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testInformation" property="testInformation" name="distributedTest" property="testInformation"/>
		<bean:define id="testType" name="distributedTest" property="testType.type"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testType" property="testType" value="<%=testType.toString()%>"/>
		<bean:define id="availableCorrection" name="distributedTest" property="correctionAvailability.availability"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.availableCorrection" property="availableCorrection" value="<%=availableCorrection.toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.imsFeedback" property="imsFeedback" name="distributedTest" property="imsFeedback"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%= distributedTestCode.toString() %>"/>
		<center>
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
		</center>
		<br/>
		<br/>
		<bean:define id="testType" name="distributedTest" property="testType.type"/>
		<jsp:include page="showStudentTest.jsp">
			<jsp:param name="pageType" value="feedback"/>
			<jsp:param name="correctionType" value=""/>
			<jsp:param name="testCode" value="<%=distributedTestCode%>"/>
	 	</jsp:include>
	</logic:present>
	<logic:notPresent name="infoStudentTestQuestionList">
		<bean:define id="infoStudentTestQuestionList" name="infoSiteStudentTestFeedback" property="infoStudentTestQuestionList"/>
		<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion" indexId="questionIndex">
			<bean:define id="question" name="testQuestion" property="question" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion"/>
			<bean:define id="questionType" name="question" property="questionType.type"/>
			<bean:define id="questionCode" name="question" property="externalId"/>
			<html:hidden alt='<%="questionCode"+questionIndex%>' property='<%="questionCode"+questionIndex%>' value="<%= questionCode.toString() %>"/>
			<html:hidden alt='<%="questionType"+questionIndex%>' property='<%="questionType"+questionIndex%>' value="<%= questionType.toString() %>"/>
			<%if(((Integer)questionType).intValue()==1 ){ %>
				<bean:define id="optionShuffle" name="testQuestion" property="optionShuffle"/>
				<html:hidden alt='<%="optionShuffle"+questionIndex%>' property='<%="optionShuffle"+questionIndex%>' value="<%= optionShuffle.toString() %>"/>
			<% } %>
			
			<logic:notEmpty name="question" name="testQuestion" property="response">
				<bean:define id="questionValue" value='<%="question"+ questionIndex%>'/>
				<%if(((Integer)questionType).intValue()==1 ){ %>
					<logic:notEmpty name="question" name="testQuestion" property="response.response">
						<logic:iterate id="r" name="testQuestion" property="response.response">
							<html:hidden alt="<%=questionValue%>" property="<%=questionValue%>" value="<%= r.toString() %>"/>
						</logic:iterate>
					</logic:notEmpty>
				<%}else{%>
				<bean:define id="r" name="testQuestion" property="response.response"/>
				<html:hidden alt="<%=questionValue%>" property="<%=questionValue%>" value="<%= r.toString() %>"/>
				<%}%>

			</logic:notEmpty>
			
		</logic:iterate>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
		<bean:define id="distributedTestCode" name="distributedTest" property="externalId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testInformation" property="testInformation" name="distributedTest" property="testInformation"/>
		<bean:define id="testType" name="distributedTest" property="testType.type"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testType" property="testType" value="<%=testType.toString()%>"/>
		<bean:define id="availableCorrection" name="distributedTest" property="correctionAvailability.availability"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.availableCorrection" property="availableCorrection" value="<%=availableCorrection.toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.imsFeedback" property="imsFeedback" name="distributedTest" property="imsFeedback"/>
		
	</logic:notPresent>
	<br/>
	<br/>
	<tr>
	<center>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.showCorrection" property="showCorrection" value="true"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.back"/></html:submit>
	</center>
	</html:form>
</logic:present>
<logic:notPresent name="infoSiteStudentTestFeedback">
	<center><h2><bean:message key="message.studentTest.notSent"/></h2></center>
</logic:notPresent>