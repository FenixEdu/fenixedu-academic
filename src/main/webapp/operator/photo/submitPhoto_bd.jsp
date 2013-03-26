<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
<h2><bean:message key="link.operator.submitPhoto"/></h2>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<p class="mtop15">
		<span class="error"><!-- Error messages go here -->
			<bean:write name="message"/>
		</span>
	</p>
</html:messages>

<html:form action="/submitPhoto.do?method=photoUpload" enctype="multipart/form-data">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

	<table class="tstyle5 thlight thright thmiddle mtop05">
		<tr>
			<th><bean:message key="property.login.username" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" size="55"/></td>
		</tr>
		<tr>
			<th><bean:message key="title.loadMarks" /></th>
			<td><html:file bundle="HTMLALT_RESOURCES" altKey="file.theFile" property="theFile" size="50"/></td>
		</tr>
	</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
	</p>
</html:form> 
