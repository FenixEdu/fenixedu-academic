<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="title.accessDelegation" /></h2>
<logic:present name="noPerson">
	<span class="error"><bean:message key="errors.noPerson" /></span>
</logic:present>
<logic:present name="noValidPerson">
	<span class="error"><bean:message key="errors.noValidPerson" /></span>
</logic:present>
<logic:present name="noUserProjects">
	<span class="error"><bean:message key="message.noUserProjects" /></span>
</logic:present>
<logic:notPresent name="noUserProjects">
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.accessDelegation.information" /></td>
		</tr>
	</table>
	<br />
	<br />
	<html:form action="/projectAccess" focus="username">
		<html:hidden property="method" value="showPersonAccesses" />
		<html:hidden property="page" value="0" />
		<table>
			<tr>
				<td><bean:message key="label.username" /></td>
				<td><html:text property="username" size="25" /></td>
				<td><html:submit styleClass="inputbutton">
					<bean:message key="label.find" />
				</html:submit></td>
			</tr>
		</table>
	</html:form>
</logic:notPresent>
