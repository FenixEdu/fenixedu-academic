<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.showAvailableQuestionsForChange" /></h2>

<logic:present name="metadataList">
	<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<logic:present name="successfulChanged">
		<logic:equal name="successfulChanged" value="false">
			<span class="error"><!-- Error messages go here --><bean:message key="message.exercise.no.variation" /></span>
		</logic:equal>
	</logic:present>

	<bean:size id="metadatasSize" name="metadataList" />
	<logic:equal name="metadatasSize" value="0">
		<span class="error"><!-- Error messages go here --><bean:message key="message.tests.no.exercises" /></span>
	</logic:equal>
	<logic:notEqual name="metadatasSize" value="0">
		<table>
			<tr>
				<td class="infoop"><bean:message key="message.showAvailableQuestionsForChange.information" /></td>
			</tr>
		</table>
		<br />
		<br />
		<html:form action="/testsManagement">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeStudentTestQuestion" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionCode" property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>" />
			<logic:present name="deleteVariation">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.deleteVariation" property="deleteVariation" value="<%=(pageContext.findAttribute("deleteVariation")).toString()%>" />
			</logic:present>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentsType" property="studentsType" value="<%=(pageContext.findAttribute("studentsType")).toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.changesType" property="changesType" value="<%=(pageContext.findAttribute("changesType")).toString()%>" />
			<table>
				<tr>
					<th class="listClasses-header"><bean:message key="label.description" /></th>
					<th class="listClasses-header"><bean:message key="label.test.materiaPrincipal" /></th>
					<th class="listClasses-header"><bean:message key="label.test.difficulty" /></th>
					<th width="90" class="listClasses-header"><bean:message key="label.test.quantidadeExercicios" /></th>
				</tr>
				<logic:iterate id="metadata" name="metadataList" type="net.sourceforge.fenixedu.domain.onlineTests.Metadata">
					<tr>
						<logic:notEqual name="metadata" property="description" value="">
							<td class="listClasses"><bean:write name="metadata" property="description" /></td>
						</logic:notEqual>
						<logic:equal name="metadata" property="description" value="">
							<td class="listClasses"><bean:message key="message.tests.notDefined" /></td>
						</logic:equal>
						<logic:notEqual name="metadata" property="mainSubject" value="">
							<td class="listClasses"><bean:write name="metadata" property="mainSubject" /></td>
						</logic:notEqual>
						<logic:equal name="metadata" property="mainSubject" value="">
							<td class="listClasses"><bean:message key="message.tests.notDefined" /></td>
						</logic:equal>
						<logic:notEqual name="metadata" property="difficulty" value="">
							<td class="listClasses"><bean:write name="metadata" property="difficulty" /></td>
						</logic:notEqual>
						<logic:equal name="metadata" property="difficulty" value="">
							<td class="listClasses"><bean:message key="message.tests.notDefined" /></td>
						</logic:equal>
						<bean:size id="numberOfMembers" name="metadata" property="visibleQuestions"/>
						<td class="listClasses"><bean:write name="numberOfMembers" /></td>
						<bean:define id="metadataCode" name="metadata" property="idInternal" />
						<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.metadataCode" property="metadataCode" value="<%= metadataCode.toString() %>" /></td>
					</tr>
				</logic:iterate>
			</table>
			<center><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="label.chosse.situation" />
			</html:submit></center>
		</html:form>
	</logic:notEqual>
</logic:present>
