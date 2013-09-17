<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
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
		<bean:define id="backendInstance" name="backendInstance" type="net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance"/>
		<html:hidden property="backendInstance" value="<%= backendInstance.name() %>"/>
		<logic:present name="infoCostCenter" scope="request">
			<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.costCenter" property="costCenter" value="<%=cc.toString()%>" />
		</logic:present>
		<logic:present name="it" scope="request">
			<logic:equal name="it" value="true">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.it" property="it" value="true" />
			</logic:equal>
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
