<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 

<strong>Pï¿½gina 3 de 6</strong>
<p align="center"><span class="error"><html:errors /></span></p>
<table cellpadding="10">
<tr align="center"><td><h2><bean:message key="schoolRegistrationHeader.title"/></h2></td></tr>
<tr><td><hr></td></tr>
<tr align="justify"><td><h2><bean:message key="schoolRegistrationHeader.lawStatement"/></h2></td></tr>
<tr><td><hr></td></tr>
<tr>
	<td></td>
	<td>Sim</td> 
	<td>Não</td>
</tr>
<html:form action="/residenceCandidacy?method=residenceCandidacy">
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="4"/>

<tr>
	<td><bean:message key="label.schoolRegistration.Question1" /></td>
	<bean:define id="answersKey"> answersMap(1) </bean:define> 
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="true" /></td>
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="false" />
		 <html:hidden alt="<%= answersKey %>" property="<%= answersKey %>" value=""/></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question2" /></td>
	<bean:define id="answersKey"> answersMap(2) </bean:define> 
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="true" /></td>
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="false" />
		 <html:hidden alt="<%= answersKey %>" property="<%= answersKey %>" value=""/></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question3" /></td>
	<bean:define id="answersKey"> answersMap(3) </bean:define> 
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="true" /></td>
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="false" />
		 <html:hidden alt="<%= answersKey %>" property="<%= answersKey %>" value=""/></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question4" /></td>
	<bean:define id="answersKey"> answersMap(4) </bean:define> 
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="true" /></td>
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="false" />
		 <html:hidden alt="<%= answersKey %>" property="<%= answersKey %>" value=""/></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question5" /></td>
	<bean:define id="answersKey"> answersMap(5) </bean:define> 
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="true" /></td>
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="false" />
		 <html:hidden alt="<%= answersKey %>" property="<%= answersKey %>" value=""/></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question6" /></td>
	<bean:define id="answersKey"> answersMap(6) </bean:define> 
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="true" /></td>
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="false" />
		 <html:hidden alt="<%= answersKey %>" property="<%= answersKey %>" value=""/></td>
</tr>
<tr>
	<td><bean:message key="label.schoolRegistration.Question7" /></td>
	<bean:define id="answersKey"> answersMap(7) </bean:define> 
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="true" /></td>
	<td><html:radio alt="<%= answersKey %>" property="<%= answersKey %>" value="false" />
		 <html:hidden alt="<%= answersKey %>" property="<%= answersKey %>" value=""/></td>
</tr>


<tr align="center">
	<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">Continuar</html:submit><td>
</tr>
</html:form>
</table>

