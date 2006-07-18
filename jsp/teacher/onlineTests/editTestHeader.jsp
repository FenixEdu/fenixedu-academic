<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="title.editTestHeader" /></h2>

<login:present name="infoTest" />

<html:form action="/testEdition">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editTestHeader" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
	<bean:define id="testCode" name="infoTest" property="idInternal" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=testCode.toString()%>" />
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.editTestHeader.information" /></td>
		</tr>
	</table>
	<br />
	<br />
	<table>
		<tr>
			<td><bean:message key="label.test.title" /></td>
		</tr>
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" size="50" name="infoTest" property="title" /></td>
			<td><span class="error"><html:errors /></span></td>
			<tr />
			<tr>
				<td><bean:message key="label.test.information" /></td>
			</tr>
			<tr>
				<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.information" rows="7" cols="45" name="infoTest" property="information" /></td>
				<tr />
	</table>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.change" />
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear" />
	</html:reset>
</html:form>
