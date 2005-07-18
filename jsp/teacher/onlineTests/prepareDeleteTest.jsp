<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="link.removeTest"/></h2>
<html:form action="/testsManagement">
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="deleteTest"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<br/>
<br/>
<bean:message key="message.confirm.deleteTest"/>
<logic:present name="title">
	&nbsp;"<bean:write name="title"/>"
</logic:present>
<bean:message key="label.questionMark"/>
<table>
<tr>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.confirm"/></html:submit></td></html:form>
	<html:form action="/testsManagement">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="showTests"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td></html:form>
</tr>
</table>