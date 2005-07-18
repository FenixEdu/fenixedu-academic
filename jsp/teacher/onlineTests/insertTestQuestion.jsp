<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table>
	<tr>
		<td class="infoop"><bean:message key="message.showExercise.information" /></td>
	</tr>
</table>
<br />
<h2><bean:message key="title.insertTestQuestionInformation" /></h2>
<br />
<logic:present name="infoQuestion">
	<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
	<bean:define id="metadata" name="infoQuestion" property="infoMetadata" />
	<bean:define id="exerciseCode" name="infoQuestion" property="idInternal" />
	<bean:define id="metadataCode" name="metadata" property="idInternal" />

	<span class="error"><html:errors /></span>

	<html:form action="/questionsManagementWithValue">
		<html:hidden property="page" value="2" />
		<html:hidden property="method" value="insertTestQuestion" />
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
		<html:hidden property="metadataCode" value="<%= metadataCode.toString() %>" />
		<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
		<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
		<table>
			<logic:notEqual name="metadata" property="description" value="">
				<tr>
					<td><b><bean:message key="label.description" />:</b></td>
					<td><bean:write name="metadata" property="description" /></td>
				</tr>
			</logic:notEqual>
			<logic:notEqual name="metadata" property="difficulty" value="">
				<tr>
					<td><b><bean:message key="label.test.difficulty" />:</b></td>
					<td><bean:write name="metadata" property="difficulty" /></td>
				</tr>
			</logic:notEqual>
			<logic:notEqual name="metadata" property="learningTime" value="">
				<tr>
					<td><b><bean:message key="label.test.learningTime" />:</b></td>
					<td><bean:write name="metadata" property="learningTimeFormatted" /></td>
				</tr>
			</logic:notEqual>
			<logic:notEqual name="metadata" property="level" value="">
				<tr>
					<td><b><bean:message key="label.exam.enrollment.year" />:</b></td>
					<td><bean:write name="metadata" property="level" /></td>
				</tr>
			</logic:notEqual>
			<logic:notEqual name="metadata" property="mainSubject" value="">
				<tr>
					<td><b><bean:message key="label.test.materiaPrincipal" />:</b></td>
					<td><bean:write name="metadata" property="mainSubject" /></td>
				</tr>
			</logic:notEqual>
			<logic:notEqual name="metadata" property="secondarySubject" value="">
				<tr>
					<td><b><bean:message key="label.test.materiaSecundaria" />:</b></td>
					<td><bean:write name="metadata" property="secondarySubject" /></td>
				</tr>
			</logic:notEqual>
			<logic:notEqual name="metadata" property="author" value="">
				<tr>
					<td><b><bean:message key="message.tests.author" /></b></td>
					<td><bean:write name="metadata" property="author" /></td>
				</tr>
			</logic:notEqual>
			<logic:notEqual name="metadata" property="numberOfMembers" value="">
				<tr>
					<td><b><bean:message key="label.test.quantidadeExercicios" />:</b></td>
					<td><bean:write name="metadata" property="numberOfMembers" /></td>
				</tr>
			</logic:notEqual>
			<tr>
				<td><b><bean:message key="message.tests.questionCardinality" />:</b></td>
				<td><bean:write name="infoQuestion" property="questionType.cardinalityType.typeString" /></td>
			</tr>
		</table>
		<br />
		<br />
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
					<td><html:radio property="formula" value="<%=formula.getValue()%>" /></td>
					<td><bean:write name="formula" property="label" filter="false" /></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<br />
		<table>
			<tr>
				<td><bean:message key="message.tests.questionValue" /></td>
				<td><html:text size="1" name="infoQuestion" property="questionValue" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.testOrder" /></td>
				<td><html:select property="questionOrder">
					<html:option value="-1">
						<bean:message key="label.end" />
					</html:option>
					<html:options name="testQuestionValues" labelName="testQuestionNames" />
				</html:select></td>
			</tr>
		</table>
		<br />
		<br />
		<table>
			<tr>
				<td><html:submit styleClass="inputbutton">
					<bean:message key="button.insert" />
				</html:submit></td>
	</html:form>
	<html:form action="/testsManagement">
		<html:hidden property="page" value="0" />
		<html:hidden property="method" value="showAvailableQuestions" />
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
		<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
		<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
		<td><html:submit styleClass="inputbutton">
			<bean:message key="link.goBack" />
		</html:submit></td>

		<tr />
		</table>
		<br />
		<br />
		<h2><bean:message key="title.example" /></h2>

		<%request.setAttribute("iquestion", pageContext.findAttribute("infoQuestion"));
        %>
		<jsp:include page="showQuestion.jsp">
			<jsp:param name="showResponses" value="true" />
		</jsp:include>

	</html:form>
</logic:present>
