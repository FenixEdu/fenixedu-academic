<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<html:form action="/findPerson.do?method=findPerson">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

	<em><bean:message key="operator" /></em>
	<h2><bean:message key="link.operator.newPassword" /></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

	<div class="infoop2">
		<bean:message key="label.operator.choosePerson"/>
	</div>

	<table class="tstyle5 thlight thright">
		<tr>
			<th>
				<bean:message key="property.login.username" />
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" />
			</td>		
		</tr>
		<tr>
			<th>
				<bean:message key="label.person.identificationDocumentNumber" />
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.documentIdNumber" property="documentIdNumber" />
			</td>		
		</tr>
	</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Seguinte" styleClass="inputbutton" property="OK"/>
	</p>
</html:form> 
