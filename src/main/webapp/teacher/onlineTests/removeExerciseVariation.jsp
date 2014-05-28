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

<h2><bean:message key="title.removeExerciseVariations" /></h2>
<br />
<logic:present name="infoQuestion">
	<logic:present name="studentsList">
		<logic:notEmpty name="studentsList">
			<table>
				<logic:iterate id="student" name="studentsList">
					<tr>
						<td><bean:write name="student" property="label" /></td>
						<td><bean:write name="student" property="value" /></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</logic:present>
	<br />

	<bean:define id="infoMetadata" name="infoQuestion" property="infoMetadata" />
	<logic:notEmpty name="infoMetadata" property="visibleQuestions">
		<bean:define id="questionsList" name="infoMetadata" property="visibleQuestions" />
		<bean:size id="questionListSize" name="questionsList" />
		<logic:greaterThan name="questionListSize" value="1">
			<bean:message key="message.removeVariation.changeStudentVariation" />
			<p></p>
			<html:form action="/exercisesEdition">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeExerciseVariation" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.variationCode" property="variationCode" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
				<logic:present name="order">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
				</logic:present>
				<logic:present name="asc">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
				</logic:present>
				<table>
					<tr>
						<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message key="button.continue" />
						</html:submit></td>
			</html:form>
		</logic:greaterThan>
		<logic:lessEqual name="questionListSize" value="1">
			<bean:message key="message.removeVariation.noMoreVariation" />
			<table>
				<tr>
		</logic:lessEqual>
	</logic:notEmpty>
	<logic:empty name="infoMetadata" property="visibleQuestions">
		<bean:message key="message.removeVariation.noMoreVariation" />
		<table>
			<tr>
	</logic:empty>
	<html:form action="/exercisesEdition">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEditExercise" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.variationCode" property="variationCode" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" />
		<logic:present name="order">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
		</logic:present>
		<logic:present name="asc">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
		</logic:present>

		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="link.goBack" />
		</html:submit></td>
		</tr>
		</table>
	</html:form>
</logic:present>
<br />

