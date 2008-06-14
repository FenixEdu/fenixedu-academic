<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.editGuide" /></h2>

<html:form action="/guideManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseGuide" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

	<table>
		<tr>
			<td><bean:message bundle="MANAGER_RESOURCES" key="label.guideYear" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.year" size="6" property="year" /></td>
		</tr>
		<tr>
			<td><bean:message bundle="MANAGER_RESOURCES" key="label.guideNumber" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.number" size="6" property="number" /></td>
		</tr>
		<tr>
			<td>Vers�o</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.version" size="6" property="version" /></td>
		</tr>		
		<tr>
			<td colspan="2"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message bundle="MANAGER_RESOURCES" key="button.choose" />
			</html:submit></td>
		</tr>
	</table>

</html:form>
