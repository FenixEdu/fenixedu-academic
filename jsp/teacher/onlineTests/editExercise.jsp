<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

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
<br />
<h2><bean:message key="title.editTestQuestion" /></h2>
<br />
<logic:present name="infoMetadata">
	<logic:present name="successfulChanged">
		<span class="error"><bean:message key="message.successfulChanged" /></span>
		<br />
		<table>
			<logic:iterate id="changed" name="successfulChanged">
				<logic:iterate id="student" name="changed" property="infoStudentList">
					<tr>
						<td><strong><bean:write name="changed" property="infoDistributedTest.title" /></strong></td>
						<td><bean:write name="student" property="number" /></td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>
		<br />
		<br />
	</logic:present>
	<bean:define id="objectCode" name="infoMetadata" property="infoExecutionCourse.idInternal" />
	<bean:define id="metadata" name="infoMetadata" />
	<bean:define id="metadataId" name="infoMetadata" property="idInternal" />
	<bean:define id="variationCode" value="<%=(pageContext.findAttribute("variationCode")).toString()%>" />
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.editExercise.information" /></td>
		</tr>
	</table>
	<html:form action="/exercisesEdition">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editExercise" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode" value="<%=(pageContext.findAttribute("metadataId")).toString()%>" />
		<logic:present name="order">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
		</logic:present>
		<logic:present name="asc">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
		</logic:present>
		<logic:notPresent name="asc">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="true" />
		</logic:notPresent>
		<table>
			<logic:notEqual name="infoMetadata" property="author" value="">
				<tr>
					<td><b><bean:message key="message.tests.author" /></b></td>
					<td><bean:write name="infoMetadata" property="author" /></td>
				</tr>
			</logic:notEqual>
			<logic:equal name="infoMetadata" property="author" value="">
				<tr>
					<td><b><bean:message key="message.tests.author" /></b></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.author" size="25" name="infoMetadata" property="author" /></td>
				</tr>
			</logic:equal>
			<tr>
				<td><b><bean:message key="label.test.quantidadeExercicios" />:</b></td>
				<td><bean:write name="infoMetadata" property="numberOfMembers" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.description" />:</b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.description" size="25" name="infoMetadata" property="description" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.test.difficulty" />:</b></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.difficulty" property="difficulty">
					<logic:notEqual name="infoMetadata" property="difficulty" value="">
						<html:option value="-1">
							<bean:write name="infoMetadata" property="difficulty" />
						</html:option>
					</logic:notEqual>
					<logic:iterate id="questionDifficulty" name="questionDifficultyList" type="org.apache.struts.util.LabelValueBean">
						<html:option value="<%=questionDifficulty.getValue()%>">
							<bean:write name="questionDifficulty" property="label" />
						</html:option>
					</logic:iterate>
				</html:select></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.test.materiaPrincipal" />:</b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mainSubject" size="25" name="infoMetadata" property="mainSubject" /></td>
			</tr>

			<tr>
				<td><b><bean:message key="label.test.materiaSecundaria" />:</b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.secondarySubject" size="25" name="infoMetadata" property="secondarySubject" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.test.learningTime" /></b><bean:message key="message.hourFormat" />:</td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.learningTimeFormatted" size="8" name="infoMetadata" property="learningTimeFormatted" /></td>
				<td><span class="error"><html:errors property="learningTimeFormatted" /></span></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.exam.enrollment.year" />:</b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.level" size="2" name="infoMetadata" property="level" /></td>
				<td><span class="error"><html:errors property="level" /></span></td>
			</tr>
		</table>
		<br />
		<table align="center">
			<tr>
				<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.change" />
				</html:submit></td>
				<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="back()">
					<bean:message key="label.back" />
				</html:submit></td>
			</tr>
		</table>
		<br />
		<br />
		<br />
		<table>
			<tr>
				<td class="infoop"><bean:message key="message.showVariation.information" /></td>
			</tr>
		</table>
		<br />
		<bean:size id="questionsSize" name="infoMetadata" property="visibleQuestions" />
		<logic:notEqual name="questionsSize" value="">
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.variationCode" property="variationCode" onchange="showQuestion()">
				<html:option value="-1">
					<bean:message key="label.variations" />
				</html:option>
				<html:option value="-2">
					<bean:message key="label.summaries.all" />
				</html:option>
				<logic:iterate id="iquestion" name="infoMetadata" property="visibleQuestions">
					<bean:define id="questionValue" name="iquestion" property="idInternal" />
					<html:option value="<%=questionValue.toString()%>">
						<bean:write name="iquestion" property="xmlFileName" />
					</html:option>
				</logic:iterate>
			</html:select>
			<br />
			<br />
			<logic:iterate id="iquestion" name="infoMetadata" property="visibleQuestions">
				<logic:notEmpty name="iquestion" property="question">
					<bean:define id="questionCode" name="iquestion" property="idInternal" />
					<bean:define id="index" value="0" />
					<bean:define id="imageLabel" value="false" />

					<h2><bean:message key="title.exercise" />:&nbsp;<bean:write name="iquestion" property="xmlFileName" /></h2>


					<div class="gen-button"><html:link
						page="<%= "/exercisesEdition.do?method=prepareRemoveExerciseVariation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;exerciseCode=" + pageContext.findAttribute("exerciseCode") + "&amp;variationCode=" + questionCode  + "&amp;order=" + pageContext.findAttribute("order")+ "&amp;asc=" + pageContext.findAttribute("asc")+"&amp;metadataCode="+metadataId%>">
						<bean:message key="label.remove" />
					</html:link></div>
					<%
					request.setAttribute("iquestion", iquestion);
					request.setAttribute("metadataId", metadataId);
			       %>
					<jsp:include page="showQuestion.jsp">
						<jsp:param name="showResponses" value="true" />
					</jsp:include>
					<br />
				</logic:notEmpty>
			</logic:iterate>
		</logic:notEqual>
	</html:form>
</logic:present>
