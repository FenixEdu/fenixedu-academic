<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.editTestQuestion"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="testQuestion" name="component" property="infoTestQuestion"/>
<bean:define id="infoQuestion" name="testQuestion" property="question"/>
<bean:define id="exerciseCode" name="infoQuestion" property="idInternal"/>
<bean:define id="testQuestionCode" name="testQuestion" property="idInternal"/>
<span class="error"><html:errors/></span>

<html:form action="/testQuestionEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="editTestQuestion"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden property="testQuestionCode" value="<%= testQuestionCode.toString() %>"/>
<html:hidden property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>"/>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.correctionFormulas.information" /></td>
	</tr>
</table>
<b><bean:message key="label.test.correctionFormulas" />:</b>
<table>
<%--<td><b><bean:message key="message.testType"/></b></td> --%>
	<logic:iterate id="formula" name="formulas" type="org.apache.struts.util.LabelValueBean">
		<tr>
 			<td><html:radio property="formula" value="<%=formula.getValue()%>"/></td>
			<td><bean:write name="formula" property="label" filter="false"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<br/>
<table>
	<tr>
	<td><b><bean:message key="message.tests.questionValue"/></b></td>
	<td><html:text size="1" name="testQuestion" property="testQuestionValue"/></td>
	</tr><tr>
		<td>
		<bean:message key="message.testOrder"/>
		</td>
		<td>
		<html:select property="testQuestionOrder">
		<html:option value="-2"><bean:message key="label.noMofify"/></html:option>
		<html:options name="testQuestionValues" labelName="testQuestionNames"/>
		<html:option value="-1"><bean:message key="label.end"/></html:option>
		</html:select>
		</td>
	</tr>
</table>
<br/>
<br/>
<table><tr>
	<td><html:submit styleClass="inputbutton"> <bean:message key="label.change"/></html:submit></td>
	</html:form>
	<html:form action="/testEdition">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="editTest"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
	<td>
		<html:submit styleClass="inputbutton"><bean:message key="link.goBack"/></html:submit>
	</td>
</tr></table>
<br/>
<br/>
<h2><bean:message key="title.example"/></h2>
<% request.setAttribute("iquestion", infoQuestion); %>
<jsp:include page="showQuestion.jsp">
	<jsp:param name="showResponses" value="true"/>
</jsp:include>
</html:form>
</logic:present>