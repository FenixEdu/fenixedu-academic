<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showAvailableQuestions"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<span class="error"><html:errors/></span>

<bean:size id="metadatasSize" name="component" property="infoMetadatas"/>
<logic:equal name="metadatasSize" value="0">
	<span class="error"><bean:message key="message.tests.no.exercices"/></span>
</logic:equal>
<logic:notEqual name="metadatasSize" value="0">
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.showAvailableQuestions.information" /></td>
		</tr>
	</table>
	<br/>
	<br/>
	<table>
	<tr>
		<td class="listClasses-header"><bean:message key="label.description"/></td>
		<td class="listClasses-header"><bean:message key="label.test.materiaPrincipal"/></td>
		<td class="listClasses-header"><bean:message key="label.test.difficulty"/></td>
		<td width="90" class="listClasses-header"><bean:message key="label.test.quantidadeExercicios"/></td>
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
		<td>
			<div class="gen-button">
			<html:link page="<%= "/questionsManagement.do?method=prepareInsertTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataCode%>">
			<bean:message key="link.see" />
			</html:link></div>
		</td>
		<td>
			<div class="gen-button">
			<html:link page="<%= "/questionsManagement.do?method=insertTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataCode%>">
			<bean:message key="button.insert" />
			</html:link></div>
		</td>
	</tr>
	</logic:iterate>
	</table>
</logic:notEqual>
<br/>
<br/>
	<table>
	<tr><td>
		<html:form action="/testEdition">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="editTest"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
			<html:submit styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
		</html:form>
	</td>
	<td>
	<html:form action="/testsManagement">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="deleteTest"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
			<html:submit styleClass="inputbutton"><bean:message key="link.removeTest"/></html:submit>
		</html:form>
	</td></tr>
	</table>
</logic:present>