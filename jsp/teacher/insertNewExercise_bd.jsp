<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 
<h2><bean:message key="title.importExercises"/></h2>
<br/>
<table>
	<tr><td class="infoop"><bean:message key="message.insertNewExercise.information" /></td></tr>
</table>
<br/>
<html:form action="/exercisesManagement" enctype="multipart/form-data">
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="loadExerciseFiles"/>
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>"/>
<logic:present name="order">
	<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
</logic:present>
<logic:present name="asc">
	<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
</logic:present>
<span class="error"><html:errors/></span>
	<table>
		<tr>
			<td><bean:message key="label.metadataFile"/></td>
		</tr>
		<tr>
			<td><html:file property="metadataFile" size="50"/></td>
		<tr/>
		<tr>
			<td><bean:message key="label.xmlZipFile"/></td>
		</tr>
		<tr>
			<td><html:file property="xmlZipFile" size="50"/></td>
		<tr/>
	</table>	
	<br />
	<br />
	<html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
	<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form> 

