<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="title.accessDelegation" /> <logic:present name="infoCostCenter" scope="request">
	&nbsp;-&nbsp;<bean:write name="infoCostCenter" property="description" />
</logic:present></h2>
<logic:present name="noPerson">
	<span class="error"><!-- Error messages go here --><bean:message key="errors.noPerson" /></span>
</logic:present>
<logic:present name="noValidPerson">
	<span class="error"><!-- Error messages go here --><bean:message key="errors.noValidPerson" /></span>
</logic:present>
<logic:present name="noUserProjects">
	<span class="error"><!-- Error messages go here --><bean:message key="message.noUserProjects" /></span>
</logic:present>
<logic:notPresent name="noUserProjects">
	<table cellspacing="0">
		<tr>
			<td class="infoop"><span class="emphasis-box">Info</span></td>
			<td class="infoop"><bean:message key="message.accessDelegation.information" /></td>
		</tr>
	</table>
	<br />
	<br />
	<html:form action="/projectAccess" focus="username">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showPersonAccesses" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
		<logic:present name="infoCostCenter" scope="request">
			<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.costCenter" property="costCenter" value="<%=cc.toString()%>" />
		</logic:present>
		<table>
			<tr>
				<td><bean:message key="label.username" /></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" size="25" /></td>
				<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.find" />
				</html:submit></td>
			</tr>
		</table>
	</html:form>
</logic:notPresent>
