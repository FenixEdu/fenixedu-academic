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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

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
<logic:present name="metadata">
	<bean:define id="objectCode" name="metadata" property="executionCourse.externalId" />
	<bean:define id="metadataId" name="metadata" property="externalId" />
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
			<logic:notEqual name="metadata" property="author" value="">
				<tr>
					<td><b><bean:message key="message.tests.author" /></b></td>
					<td><bean:write name="metadata" property="author" /></td>
				</tr>
			</logic:notEqual>
			<logic:equal name="metadata" property="author" value="">
				<tr>
					<td><b><bean:message key="message.tests.author" /></b></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.author" size="25" name="metadata" property="author" /></td>
				</tr>
			</logic:equal>
			<tr>
				<td><b><bean:message key="label.test.quantidadeExercicios" />:</b></td>
				<bean:size id="questionsSize" name="metadata" property="visibleQuestions"/>
				<td><bean:write name="questionsSize" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.description" />:</b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.description" size="25" name="metadata" property="description" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.test.difficulty" />:</b></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.difficulty" property="difficulty">
					<logic:notEqual name="metadata" property="difficulty" value="">
						<html:option value="-1">
							<bean:write name="metadata" property="difficulty" />
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
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mainSubject" size="25" name="metadata" property="mainSubject" /></td>
			</tr>

			<tr>
				<td><b><bean:message key="label.test.materiaSecundaria" />:</b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.secondarySubject" size="25" name="metadata" property="secondarySubject" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.test.learningTime" /></b><bean:message key="message.hourFormat" />:</td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.learningTimeFormatted" size="8" name="metadata" property="learningTimeFormatted" /></td>
				<td><span class="error"><html:errors property="learningTimeFormatted" /></span></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.exam.enrollment.year" />:</b></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.level" size="2" name="metadata" property="level" /></td>
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
		<bean:size id="questionsSize" name="metadata" property="visibleQuestions" />
		<logic:notEqual name="questionsSize" value="">
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.variationCode" property="variationCode" onchange="showQuestion()">
				<html:option value="-1">
					<bean:message key="label.variations" />
				</html:option>
				<html:option value="-2">
					<bean:message key="label.summaries.all" />
				</html:option>
				<logic:iterate id="iquestion" name="metadata" property="visibleQuestions">
					<bean:define id="questionValue" name="iquestion" property="externalId" />
					<html:option value="<%=questionValue.toString()%>">
						<bean:write name="iquestion" property="xmlFileName" />
					</html:option>
				</logic:iterate>
			</html:select>
			<br />
			<br />
			<logic:equal name="variationCode" value="-2">
				<logic:iterate id="iquestion" name="metadata" property="visibleQuestions">
					<bean:define id="questionCode" name="iquestion" property="externalId" />
					<bean:define id="index" value="0" />
					<bean:define id="imageLabel" value="false" />
					<h2><bean:message key="title.exercise" />:&nbsp;<bean:write name="iquestion" property="xmlFileName" /></h2>
					<div class="gen-button"><html:link
						page="<%= "/exercisesEdition.do?method=prepareRemoveExerciseVariation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;exerciseCode=" + pageContext.findAttribute("exerciseCode") + "&amp;variationCode=" + questionCode  + "&amp;order=" + pageContext.findAttribute("order")+ "&amp;asc=" + pageContext.findAttribute("asc")%>">
						<bean:message key="label.remove" />
					</html:link></div>
					<div class="gen-button"><html:link
						page="<%= "/exercisesEdition.do?method=exportExerciseVariation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;exerciseCode=" + pageContext.findAttribute("exerciseCode") + "&amp;variationCode=" + questionCode  + "&amp;order=" + pageContext.findAttribute("order")+ "&amp;asc=" + pageContext.findAttribute("asc")%>">
						<bean:message key="link.export" />
					</html:link></div>
					<%request.setAttribute("iquestion", iquestion);
					request.setAttribute("metadataId", metadataId);%>
					<jsp:include page="showQuestion.jsp">
						<jsp:param name="showResponses" value="true" />
					</jsp:include>
				</logic:iterate>
			</logic:equal>
			<logic:notEqual name="variationCode" value="-2">
			<logic:iterate id="iquestion" name="metadata" property="visibleQuestions">
				<logic:equal name="iquestion" property="externalId" value="<%= variationCode.toString()%>">
					<bean:define id="questionCode" name="iquestion" property="externalId" />
					<bean:define id="index" value="0" />
					<bean:define id="imageLabel" value="false" />
					<h2><bean:message key="title.exercise" />:&nbsp;<bean:write name="iquestion" property="xmlFileName" /></h2>
					<div class="gen-button"><html:link
						page="<%= "/exercisesEdition.do?method=prepareRemoveExerciseVariation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;exerciseCode=" + pageContext.findAttribute("exerciseCode") + "&amp;variationCode=" + questionCode  + "&amp;order=" + pageContext.findAttribute("order")+ "&amp;asc=" + pageContext.findAttribute("asc")%>">
						<bean:message key="label.remove" />
					</html:link></div>
					<div class="gen-button"><html:link
						page="<%= "/exercisesEdition.do?method=exportExerciseVariation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;exerciseCode=" + pageContext.findAttribute("exerciseCode") + "&amp;variationCode=" + questionCode  + "&amp;order=" + pageContext.findAttribute("order")+ "&amp;asc=" + pageContext.findAttribute("asc")%>">
						<bean:message key="link.export" />
					</html:link></div>
					<%request.setAttribute("iquestion", iquestion);
					request.setAttribute("metadataId", metadataId);%>
					<jsp:include page="showQuestion.jsp">
						<jsp:param name="showResponses" value="true" />
					</jsp:include>
					<br />
				</logic:equal>
			</logic:iterate>
			</logic:notEqual>
		</logic:notEqual>
	</html:form>
</logic:present>
