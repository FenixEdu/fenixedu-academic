<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 


<table cellpadding="10">
<tr align="center"><td><h2><bean:message key="schoolRegistrationHeader.title" bundle="STUDENT_RESOURCES"/></h2></td></tr>
<tr><td><hr></td></tr>
<tr align="justify"><td><h2><bean:message key="schoolRegistrationHeader.lawStatement" bundle="STUDENT_RESOURCES"/></h2></td></tr>
<tr><td><hr></td></tr>
<tr>
	<td></td>
	<td>Sim</td> 
	<td>Não</td>
</tr>
<html:form action="/enrollStudent?method=enrollStudent">

<tr>
	<td><bean:message key="label.schoolRegistration.Question1" bundle="STUDENT_RESOURCES"/></td>
	<bean:define id="answersKey"> answersMap(1) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question2" bundle="STUDENT_RESOURCES"/></td>
	<bean:define id="answersKey"> answersMap(2) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question3" bundle="STUDENT_RESOURCES"/></td>
	<bean:define id="answersKey"> answersMap(3) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question4" bundle="STUDENT_RESOURCES"/></td>
	<bean:define id="answersKey"> answersMap(4) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question5" bundle="STUDENT_RESOURCES"/></td>
	<bean:define id="answersKey"> answersMap(5) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question6" bundle="STUDENT_RESOURCES"/></td>
	<bean:define id="answersKey"> answersMap(6) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question7" bundle="STUDENT_RESOURCES"/></td>
	<bean:define id="answersKey"> answersMap(7) </bean:define> 
	<td><html:radio property="<%= answersKey %>" value="true" /></td>
	<td><html:radio property="<%= answersKey %>" value="false" /></td>
</tr>


<tr align="center">
	<td><html:submit styleClass="inputbutton">Continuar</html:submit><td>
</tr>
</html:form>
</table>

