<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="link.removeTestQuestion"/></h2>
<html:form action="/exercisesManagement">
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="removeExercise"/>
<html:hidden property="exerciseCode" value="<%=(pageContext.findAttribute("exerciseCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
<br/>
<br/>
<bean:message key="message.confirm.deleteExercise"/>
<table>
<tr>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.confirm"/></html:submit></td></html:form>
	<html:form action="/exercisesManagement">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="exercisesFirstPage"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
	<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td></html:form>
</tr>
</table>