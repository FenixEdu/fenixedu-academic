<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script language="Javascript" type="text/javascript">
<!--
function showQuestion(){
	document.forms[0].method.value="prepareEditExercise";
	document.forms[0].submit();
}
function back(){
	document.forms[0].method.value="exercisesFirstPage";
}
// -->
</script>
<br/>
<h2><bean:message key="title.editTestQuestion"/></h2>
<br/>
<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="metadata" name="component" property="infoMetadata"/>
<bean:define id="metadataId" name="metadata" property="idInternal"/>
<bean:define id="variationCode" value="<%=(pageContext.findAttribute("variationCode")).toString()%>"/>
<table>
	<tr><td class="infoop"><bean:message key="message.editExercise.information" /></td></tr>
</table>
<html:form action="/exercisesEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="editExercise"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="exerciseCode" value="<%=(pageContext.findAttribute("metadataId")).toString()%>"/>
<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
<table>
		<logic:notEqual name="metadata" property="author" value="">
			<tr>
				<td><b><bean:message key="message.tests.author"/></b></td>
				<td><bean:write name="metadata" property="author"/></td>
			</tr>
		</logic:notEqual>
		<logic:equal name="metadata" property="author" value="">
			<tr>
				<td><b><bean:message key="message.tests.author"/></b></td>
				<td><html:text size="25" name="metadata" property="author"/></td>
			</tr>
		</logic:equal>
		<tr>
			<td><b><bean:message key="label.test.quantidadeExercicios"/>:</b></td>
			<td><bean:write name="metadata" property="numberOfMembers"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="label.description"/>:</b></td>
			<td><html:text size="25" name="metadata" property="description"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="label.test.difficulty"/>:</b></td>
			<td><html:select property="difficulty">
				<logic:notEqual name="metadata" property="difficulty" value="">
					<html:option value="-1"><bean:write name="metadata" property="difficulty"/></html:option>
				</logic:notEqual>
				<logic:iterate id="questionDifficulty" name="questionDifficultyList" type="org.apache.struts.util.LabelValueBean">
					<html:option value="<%=questionDifficulty.getValue()%>"><bean:write name="questionDifficulty" property="label"/></html:option>
				</logic:iterate>
			</html:select></td>
		</tr>	
		<tr>
			<td><b><bean:message key="label.test.materiaPrincipal"/>:</b></td>
			<td><html:text size="25" name="metadata" property="mainSubject"/></td>
		</tr>
	
		<tr>
			<td><b><bean:message key="label.test.materiaSecundaria"/>:</b></td>
			<td><html:text size="25" name="metadata" property="secondarySubject"/></td>
		</tr>
		<tr>
			<td><b><bean:message key="label.test.learningTime"/></b><bean:message key="message.hourFormat"/>:</td>
			<td><html:text size="8" name="metadata" property="learningTimeFormatted"/></td>
			<td><span class="error"><html:errors property="learningTimeFormatted"/></span></td>
		</tr>	
		<tr>
			<td><b><bean:message key="label.exam.enrollment.year"/>:</b></td>
			<td><html:text size="2" name="metadata" property="level"/></td>
			<td><span class="error"><html:errors property="level"/></span></td>
		</tr>
</table>
<br/>
<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton"><bean:message key="label.change"/></html:submit> </td>
		<td><html:submit styleClass="inputbutton" onclick="back()"><bean:message key="label.back"/></html:submit></td>
	</tr>
</table>
<br/>
<br/>
<br/>
<table>
	<tr><td class="infoop"><bean:message key="message.showVariation.information" /></td></tr>
</table>
<br/>
<bean:define id="questionName" name="component" property="questionNames"/>
<bean:size id="questionsSize" name="questionName"/>
<logic:notEqual name="questionsSize" value="">
	<html:select property="variationCode" onchange="showQuestion()">
		<html:option value="-1"><bean:message key="label.variations"/></html:option>
		<html:option value="-2"><bean:message key="label.summaries.all"/></html:option>
		<logic:iterate id="question" name="questionName">
			<bean:define id="questionValue" name="question" property="value"/>
			<html:option value="<%=questionValue.toString()%>"><bean:write name="question" property="label"/></html:option>
		</logic:iterate>
	</html:select>
	<br/>
	<br/>
	<logic:iterate id="iquestion" name="metadata" property="visibleQuestions">
		<bean:define id="questionCode" name="iquestion" property="idInternal"/>
		<bean:define id="index" value="0"/>
		<bean:define id="imageLabel" value="false"/>
		<logic:iterate id="questionBody" name="iquestion" property="question" length="1">
			<h2><bean:message key="title.exercise"/>:&nbsp;<bean:write name="iquestion" property="xmlFileName"/></h2>
		</logic:iterate>
		<% request.setAttribute("iquestion", iquestion); %>
		<jsp:include page="showQuestion.jsp">
			<jsp:param name="showResponses" value="true"/>
		</jsp:include>
		<br/>
	</logic:iterate>
</logic:notEqual>
</html:form>
</logic:present>