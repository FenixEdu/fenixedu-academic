<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="link.removeTestQuestion"/></h2>
<html:form action="/exercicesManagement">
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="removeExercice"/>
<html:hidden property="exerciceCode" value="<%=(pageContext.findAttribute("exerciceCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<br/>
<br/>
<bean:message key="message.confirm.deleteExercice"/>
<table>
<tr>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.confirm"/></html:submit></td></html:form>
	<html:form action="/exercicesManagement">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="exercicesFirstPage"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td></html:form>
</tr>
</table>