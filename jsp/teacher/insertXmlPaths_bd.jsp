<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 
<h2><bean:message key="title.insertExercices"/></h2>

<html:form action="/exercicesManagement" enctype="multipart/form-data">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="loadXmlFiles"/>
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<span class="error"><html:errors/></span>
	<table>
		<tr>
			<td><bean:message key="label.exercicePath"/></td>
		</tr>
		<logic:iterate id="path" name="xmlNames" indexId="index" type="java.lang.String">
			<tr>
				<td><html:file property="xmlFiles" indexed="true" size="50"/></td>
			<tr/>
		</logic:iterate>
	</table>	
	<br />
	<br />
	<html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
	<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form> 

