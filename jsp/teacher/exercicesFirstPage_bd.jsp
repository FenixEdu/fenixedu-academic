<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="link.exerciceManagement"/></h2>

<logic:present name="badXmls">
	<bean:size id="badXmlsListSize" name="badXmls"/>
	<logic:equal name="badXmlsListSize" value="0">
		<span class="error"><bean:message key="message.sucessfullInsert"/></span>
	</logic:equal>
	<logic:notEqual name="badXmlsListSize" value="0">
		<span class="error"><bean:message key="message.not.insertedList"/></span>
		<table>
		<logic:iterate id="xmlName" name="badXmls">
			<tr><td><bean:write name="xmlName"/></td></tr>
		</logic:iterate>
		</table>
	</logic:notEqual>
</logic:present>
<br/>
<br/>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<span class="error"><html:errors/></span>

<bean:size id="metadatasSize" name="component" property="infoMetadatas"/>
<logic:equal name="metadatasSize" value="0">
	<span class="error"><bean:message key="message.tests.no.exercices"/></span>
</logic:equal>
<table>
	<tr><td>
		<div class="gen-button">
			<html:link page="<%= "/exercicesManagement.do?method=insertNewExercice&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
				<bean:message key="label.test.insertQuestion" />
			</html:link>
		</div>
	</td></tr>
</table>
<logic:notEqual name="metadatasSize" value="0">
	<br/>
	<br/>
	<table>
	<tr>
		<td width="150" class="listClasses-header"><bean:message key="label.test.materiaPrincipal"/></td>
		<td width="150" class="listClasses-header"><bean:message key="label.test.difficulty"/></td>
		<td width="90" class="listClasses-header"><bean:message key="label.test.quantidadeExercicios"/></td>
	</tr>
	<logic:iterate id="metadata" name="component" property="infoMetadatas" type="DataBeans.InfoMetadata">
	<tr>
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
		<bean:size id="quantidadeExercicios" name="metadata" property="members"/> 
		<td class="listClasses"><bean:write name="quantidadeExercicios"/></td>		
		<bean:define id="metadataCode" name="metadata" property="idInternal" />
	</tr>
	</logic:iterate>
	</table>
</logic:notEqual>
</logic:present>