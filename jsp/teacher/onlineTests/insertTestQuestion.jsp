<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
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
<logic:present name="question">
	<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
	<bean:define id="metadata" name="question" property="metadata" />
	<bean:define id="exerciseCode" name="question" property="idInternal" />
	<bean:define id="metadataCode" name="metadata" property="idInternal" />
	<logic:iterate id="subQuestion" name="question" property="subQuestions" />
	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<html:form action="/questionsManagementWithValue">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertTestQuestion" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.metadataCode" property="metadataCode" value="<%= metadataCode.toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
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
			<tr>
				<td><b><bean:message key="label.test.quantidadeExercicios" />:</b></td>
				<bean:size id="numberOfMembers" name="metadata" property="visibleQuestions" />
				<td><bean:write name="numberOfMembers" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="message.tests.questionCardinality" />:</b></td>
				<td><bean:write name="subQuestion" property="questionType.cardinalityType.typeString" /></td>
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
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.formula" property="formula" value="<%=formula.getValue()%>" /></td>
					<td><bean:write name="formula" property="label" filter="false" /></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<br />
		<table>
			<tr>
				<td><bean:message key="message.tests.questionValue" /></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.questionValue" size="1" name="subQuestion" property="questionValue" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.testOrder" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.questionOrder" property="questionOrder">
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
				<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.insert" />
				</html:submit></td>
	</html:form>
	<html:form action="/testsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showAvailableQuestions" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="link.goBack" />
		</html:submit></td>

		<tr />
		</table>
		<br />
		<br />
		<h2><bean:message key="title.example" /></h2>

		<%request.setAttribute("iquestion", pageContext.findAttribute("question"));
		request.setAttribute("metadataId", metadataCode);
        %>
		<jsp:include page="showQuestion.jsp">
			<jsp:param name="showResponses" value="true" />
		</jsp:include>

	</html:form>
</logic:present>
