<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

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

