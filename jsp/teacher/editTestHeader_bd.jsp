<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.editTestHeader"/></h2>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="infoTest" name="component" property="infoTest"/>

<html:form action="/testEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="editTestHeader"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.editTestHeader.information" /></td>
	</tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><bean:message key="label.test.title"/></td>
	</tr>
	<tr>
		<td><html:text size="50" name="infoTest" property="title"/></td>
		<td><span class="error"><html:errors/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="label.test.information"/></td>
	</tr>
	<tr>
		<td><html:textarea rows="7" cols="45" name="infoTest" property="information"/></td>
	<tr/>
</table>
<html:submit styleClass="inputbutton"><bean:message key="label.change"/></html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>
