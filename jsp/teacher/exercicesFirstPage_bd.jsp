<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="link.showExercices"/></h2>

<logic:present name="badXmls">
	<bean:size id="badXmlsListSize" name="badXmls"/>
	<logic:equal name="badXmlsListSize" value="0">
		<span class="error"><bean:message key="message.sucessfullInsert"/></span>
	</logic:equal>
	<logic:notEqual name="badXmlsListSize" value="0">
		<logic:iterate id="xmlName" name="badXmls" indexId="index">
			<logic:equal name="index" value="0">
				<logic:equal name="xmlName" value="badMetadata">
					<span class="error"><bean:message key="message.not.insertedExercice"/></span>
					<table>
				</logic:equal>
				<logic:notEqual name="xmlName" value="badMetadata">
					<span class="error"><bean:message key="message.not.insertedList"/></span>
					<table>
				</logic:notEqual>
			</logic:equal>
			<logic:notEqual name="xmlName" value="badMetadata">
				<tr><td><bean:write name="xmlName"/></td></tr>
			</logic:notEqual>
		</logic:iterate>
		</table>
	</logic:notEqual>
</logic:present>
<logic:present name="successfulDeletion">
	<logic:equal name="successfulDeletion" value="true">
		<span class="error"><bean:message key="message.successulDeletion"/></span>
	</logic:equal>
</logic:present>
<br/>
<br/>
<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<span class="error"><html:errors/></span>
<table>
	<tr><td class="infoop"><bean:message key="message.exercicesFirstPage.information" /></td></tr>
</table>
<br/>
<bean:size id="metadatasSize" name="component" property="infoMetadatas"/>
<logic:equal name="metadatasSize" value="0">
	<span class="error"><bean:message key="message.tests.no.exercices"/></span>
</logic:equal>
<table>
	<tr><td>
		<div class="gen-button">
			<html:link page="<%= "/exercicesManagement.do?method=insertNewExercice&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
				<bean:message key="link.importExercice" />
			</html:link>
		</div>
	</td></tr>
</table>
<logic:notEqual name="metadatasSize" value="0">
	<br/>
	<br/>
	<table>
	<tr>
		<td class="listClasses-header">
			<div class="gen-button">
			<html:link page="<%= "/exercicesManagement.do?method=exercicesFirstPage&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=description"%>">
			<bean:message key="label.description"/>
			</html:link></div>
		</td>		
		<td class="listClasses-header">
			<div class="gen-button">
			<html:link page="<%= "/exercicesManagement.do?method=exercicesFirstPage&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;order=mainSubject"%>">
			<bean:message key="label.test.materiaPrincipal"/>
			</html:link></div>
		</td>
		<td class="listClasses-header">
			<div class="gen-button">
			<html:link page="<%= "/exercicesManagement.do?method=exercicesFirstPage&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;order=difficulty"%>">
			<bean:message key="label.test.difficulty"/>
			</html:link></div>
		</td>
		<td width="90" class="listClasses-header">
			<div class="gen-button">
			<html:link page="<%= "/exercicesManagement.do?method=exercicesFirstPage&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;order=numberOfMembers"%>">
			<bean:message key="label.test.quantidadeExercicios"/>
			</html:link></div>
		</td>
		
	</tr>

	</tr>
	<logic:iterate id="metadata" name="component" property="infoMetadatas" type="DataBeans.InfoMetadata">
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
		<td><div class="gen-button">
			<bean:define id="exerciceCode" name="metadata" property="idInternal"/>
			<html:link page="<%= "/exercicesManagement.do?method=prepareRemoveExercice&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;exerciceCode=" + pageContext.findAttribute("exerciceCode")%>">
				<bean:message key="label.delete" />
			</html:link>
		</div></td>
	</tr>
	</logic:iterate>
	</table>
</logic:notEqual>
</logic:present>