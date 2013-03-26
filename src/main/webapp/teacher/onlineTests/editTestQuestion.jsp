<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.editTestQuestion" /></h2>

<logic:present name="testQuestion">
	<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<bean:define id="question" name="testQuestion" property="question" />
	<bean:define id="exerciseCode" name="question" property="idInternal" />
	<bean:define id="metadataId" name="question" property="metadata.idInternal" />
	<bean:define id="testQuestionCode" name="testQuestion" property="idInternal" />
	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<html:form action="/testQuestionEdition">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editTestQuestion" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testQuestionCode" property="testQuestionCode" value="<%= testQuestionCode.toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionCode" property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>" />
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
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.formula" property="formula" value="<%=formula.getValue()%>" /></td>
					<td><bean:write name="formula" property="label" filter="false" /></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<br />
		<table>
			<tr>
				<td><b><bean:message key="message.tests.questionValue" /></b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.testQuestionValue" size="1" name="testQuestion" property="testQuestionValue" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.testOrder" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.testQuestionOrder" property="testQuestionOrder">
					<html:option value="-2">
						<bean:message key="label.noMofify" />
					</html:option>
					<html:options name="testQuestionValues" labelName="testQuestionNames" />
					<html:option value="-1">
						<bean:message key="label.end" />
					</html:option>
				</html:select></td>
			</tr>
		</table>
		<br />
		<br />
		<table>
			<tr>
				<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.change" />
				</html:submit></td>
	</html:form>
	<html:form action="/testEdition">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editTest" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="link.goBack" />
		</html:submit></td>
		</tr>
		</table>
		<br />
		<br />
		<h2><bean:message key="title.example" /></h2>
		<%request.setAttribute("iquestion", question);
		request.setAttribute("metadataId", metadataId);
        %>
		<jsp:include page="showQuestion.jsp">
			<jsp:param name="showResponses" value="true" />
		</jsp:include>
	</html:form>
</logic:present>
