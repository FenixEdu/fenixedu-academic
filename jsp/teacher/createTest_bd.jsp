<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.createTest"/></h2>

<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>

<bean:size id="metadatasSize" name="component" property="infoMetadatas"/>
<logic:equal name="metadatasSize" value="0">
	<span class="error"><bean:message key="message.tests.no.exercices"/></span>
</logic:equal>
<logic:notEqual name="metadatasSize" value="0">

<html:form action="/testsManagement">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="createTest"/>
<html:hidden property="objectCode"/>

<table>
	<tr>
		<td class="infoop"><bean:message key="message.createTest.information" /></td>
	</tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><bean:message key="label.test.title"/></td>
	</tr>
	<tr>
		<td><html:text size="50" property="title"/><span class="error"><html:errors/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="label.test.information"/></td>
	</tr>
	<tr>
		<td><html:textarea rows="7" cols="45" property="information"/></td>
	<tr/>
</table>
<br/>
<br/>
<html:submit styleClass="inputbutton"><bean:message key="button.confirm"/></html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>
</logic:notEqual>
