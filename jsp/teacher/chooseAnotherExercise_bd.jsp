<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showAvailableQuestionsForChange"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<span class="error"><html:errors/></span>

<logic:present name="successfulChanged">
	<logic:equal name="successfulChanged" value="false">
		<span class="error"><bean:message key="message.exercise.no.variation"/></span>
	</logic:equal>
</logic:present> 

<bean:size id="metadatasSize" name="component" property="infoMetadatas"/>
<logic:equal name="metadatasSize" value="0">
	<span class="error"><bean:message key="message.tests.no.exercises"/></span>
</logic:equal>
<logic:notEqual name="metadatasSize" value="0">
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.showAvailableQuestionsForChange.information" /></td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:form action="/testsManagement">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="changeStudentTestQuestion"/>
	<html:hidden property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<html:hidden property="studentCode" value="<%=(pageContext.findAttribute("studentCode")).toString()%>"/>
	<html:hidden property="questionCode" value="<%=(pageContext.findAttribute("questionCode")).toString()%>"/>
	<logic:present name="deleteVariation"> 
		<html:hidden property="deleteVariation" value="<%=(pageContext.findAttribute("deleteVariation")).toString()%>"/>
	</logic:present> 
	<html:hidden property="studentsType" value="<%=(pageContext.findAttribute("studentsType")).toString()%>"/>
	<html:hidden property="changesType" value="<%=(pageContext.findAttribute("changesType")).toString()%>"/>
	<table>
	<tr>
		<td class="listClasses-header"><bean:message key="label.description"/></td>
		<td class="listClasses-header"><bean:message key="label.test.materiaPrincipal"/></td>
		<td class="listClasses-header"><bean:message key="label.test.difficulty"/></td>
		<td width="90" class="listClasses-header"><bean:message key="label.test.quantidadeExercicios"/></td>
	</tr>
	<logic:iterate id="metadata" name="component" property="infoMetadatas" type="net.sourceforge.fenixedu.dataTransferObject.InfoMetadata">
	<tr>
		<logic:notEqual name="metadata" property="description" value="">
			<td class="listClasses"><bean:write name="metadata" property="description"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="description" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>
		<logic:notEqual name="metadata" property="mainSubject" value="">
			<td class="listClasses"><bean:write name="metadata" property="mainSubject"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="mainSubject" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>
		<logic:notEqual name="metadata" property="difficulty" value="">
			<td class="listClasses"><bean:write name="metadata" property="difficulty"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="difficulty" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>
		<logic:notEqual name="metadata" property="numberOfMembers" value="">
			<td class="listClasses"><bean:write name="metadata" property="numberOfMembers"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="numberOfMembers" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>	
		<bean:define id="metadataCode" name="metadata" property="idInternal" />
		
		<td><html:radio property="metadataCode" value="<%= metadataCode.toString() %>"/></td>	
	</tr>
	</logic:iterate>
	</table>
	<center><html:submit styleClass="inputbutton"><bean:message key="label.chosse.situation"/></html:submit></center>
	</html:form>
</logic:notEqual>
</logic:present>