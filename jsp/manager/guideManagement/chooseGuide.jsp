<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.editGuide" /></h2>

<html:form action="/guideManagement">
	<html:hidden property="method" value="chooseGuide" />
	<html:hidden property="page" value="1" />

	<table>
		<tr>
			<td><bean:message bundle="MANAGER_RESOURCES" key="label.guideYear" /></td>
			<td><html:text size="6" property="year" /></td>
		</tr>
		<tr>
			<td><bean:message bundle="MANAGER_RESOURCES" key="label.guideNumber" /></td>
			<td><html:text size="6" property="number" /></td>
		</tr>
		<tr>
			<td>Versão</td>
			<td><html:text size="6" property="version" /></td>
		</tr>		
		<tr>
			<td colspan="2"><html:submit styleClass="inputbutton">
				<bean:message bundle="MANAGER_RESOURCES" key="button.choose" />
			</html:submit></td>
		</tr>
	</table>

</html:form>
