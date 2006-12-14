<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 
<h2><bean:message key="title.importExercises"/></h2>
<br/>
<table>
	<tr><td class="infoop"><bean:message key="message.insertNewExercise.information" /></td></tr>
</table>
<br/>
<html:form action="/exercisesManagement" enctype="multipart/form-data">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="loadExerciseFiles"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>"/>
<logic:present name="order">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
</logic:present>
<logic:present name="asc">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
</logic:present>
<span class="error"><!-- Error messages go here --><html:errors /></span>
	<table>
		<tr>
			<td><bean:message key="label.xmlZipFile"/></td>
		</tr>
		<tr>
			<td><html:file bundle="HTMLALT_RESOURCES" altKey="file.xmlZipFile" property="xmlZipFile" size="50"/></td>
		</tr>
	</table>	
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form> 

