<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="link.createTest"/></h2>

<logic:present name="availableMetadatas">
	<logic:equal name="availableMetadatas" value="0">
		<span class="error"><bean:message key="message.tests.no.exercises"/></span>
	</logic:equal>
	
	<logic:notEqual name="availableMetadatas" value="0">
		<html:form action="/testsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createTest"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>

		<table><tr><td class="infoop"><bean:message key="message.createTest.information" /></td></tr></table>
		<br/><br/>
		<table>
			<tr><td><bean:message key="label.test.title"/></td></tr>
			<tr><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" size="50" property="title"/><span class="error"><!-- Error messages go here --><html:errors /></span></td><tr/>
			<tr><td><bean:message key="label.test.information"/></td></tr>
			<tr><td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.information" rows="7" cols="45" property="information"/></td><tr/>
		</table>
		<br/><br/>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit> 
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
		</html:form>
	</logic:notEqual>
</logic:present>
<logic:notPresent name="availableMetadatas">
	<span class="error"><bean:message key="message.tests.no.exercises"/></span>
</logic:notPresent>