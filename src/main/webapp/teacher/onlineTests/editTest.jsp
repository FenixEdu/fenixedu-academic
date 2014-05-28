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
<h2><bean:message key="title.editTest"/></h2>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.editTest.information" /></td>
	</tr>
</table>
<br/>
<logic:present name="test">
<bean:define id="testQuestionList" name="testQuestionList"/>

<html:form action="/testEdition">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editTest"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
<table>
	<tr>
		<td><b><bean:message key="label.title"/></b></td>
		<td><bean:write name="test" property="title"/></td>
	</tr>
	<logic:notEqual name="test" property="information" value="">
		<tr>
			<td><b><bean:message key="label.information"/></b></td>
			<td><bean:write name="test" property="information"/></td>
		</tr>
	</logic:notEqual>
</table>
<table>
	<tr>
		<td><div class="gen-button">
		<html:link page="<%= "/testEdition.do?method=prepareEditTestHeader&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="link.editTestHeader" />
		</html:link>&nbsp;&nbsp;&nbsp;</div></td>
		<td><div class="gen-button">
		<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="label.test.insertQuestion"/>
		</html:link>&nbsp;&nbsp;&nbsp;</div></td>
		<td><div class="gen-button">
		<html:link page="<%= "/testsManagement.do?method=showTests&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
		<bean:message key="label.finish"/>
		</html:link></div></td>
	</tr>
</table>
<br/>
<table>
	<tr><td><hr/></td></tr>
	<logic:iterate id="testQuestion" name="testQuestionList" type="net.sourceforge.fenixedu.domain.onlineTests.TestQuestion">
	<tr>
		<td><b><bean:message key="message.tests.question" />:</b>&nbsp;<bean:write name="testQuestion" property="testQuestionOrder"/></td></tr>
		<bean:define id="testQuestionValue" name="testQuestion" property="testQuestionValue"/>
		<bean:define id="testQuestionValue" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(testQuestionValue.toString())).toString()) %>"/>		
		<tr><td><b><bean:message key="message.tests.questionValue" /></b>&nbsp;<bean:write name="testQuestionValue"/></td></tr>
		<bean:define id="thisQuestion" name="testQuestion" property="question" type="net.sourceforge.fenixedu.domain.onlineTests.Question"/>
		<bean:define id="questionCode" name="thisQuestion" property="externalId"/>
		<tr><td><table><tr><td>
			<div class="gen-button">
			<html:link page="<%= "/testQuestionEdition.do?method=prepareEditTestQuestion&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode") +"&amp;questionCode=" +questionCode%>">
			<bean:message key="title.editTestQuestion" />
			</html:link>&nbsp;&nbsp;&nbsp;</div></td>
			<td><div class="gen-button">
			<html:link page="<%= "/testEdition.do?method=deleteTestQuestion&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode") +"&amp;questionCode=" +questionCode%>">
			<bean:message key="link.removeTestQuestion" />
			</html:link></div></td>
		</tr></table><tr><td>
		<bean:define id="metadataId" name="thisQuestion" property="metadata.externalId"/>
		<% request.setAttribute("iquestion", thisQuestion); 
		request.setAttribute("metadataId", metadataId);%>
		<jsp:include page="showQuestion.jsp">
			<jsp:param name="showResponses" value="false"/>
		</jsp:include>
	</td></tr>
	<tr><td><hr/></td></tr>
	</logic:iterate>
</table>
<br/>
<bean:size id="testQuestionListSize" name="test" property="testQuestions"/>
<logic:notEqual name="testQuestionListSize" value="0">
	<table>
		<tr><td>
			<div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
			<bean:message key="label.test.insertQuestion" />
			</html:link>&nbsp;&nbsp;&nbsp;</div></td>
			<td><div class="gen-button">
			<html:link page="<%= "/testsManagement.do?method=showTests&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")%>">
			<bean:message key="label.finish" />
			</html:link></div></td>
		</tr>
	</table>
</logic:notEqual>
</html:form>
</logic:present>
