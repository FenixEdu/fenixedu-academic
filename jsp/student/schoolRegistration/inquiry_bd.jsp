<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 


<table cellpadding="10">
<tr>
	<td></td>
	<td>Sim</td>
	<td>Não</td>
</tr>
<html:form action="/schoolRegistrationInquiry">

<tr>
	<td>Pergunta1</td>
	<bean:define id="answersKey"> answersMap(1) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td>Pergunta2</td>
	<bean:define id="answersKey"> answersMap(2) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td>Pergunta3</td>
	<bean:define id="answersKey"> answersMap(3) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td>Pergunta4</td>
	<bean:define id="answersKey"> answersMap(4) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td>Pergunta5</td>
	<bean:define id="answersKey"> answersMap(5) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td>Pergunta6</td>
	<bean:define id="answersKey"> answersMap(6) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td>Pergunta7</td>
	<bean:define id="answersKey"> answersMap(7) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>

<tr>
	<td><html:submit styleClass="inputbutton">Continuar</html:submit><td>
</tr>
</html:form>
</table>

