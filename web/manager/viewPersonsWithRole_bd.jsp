<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.roles"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present name="<%= PresentationConstants.ROLES %>" scope="request">
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="label.roleType"/>
			</th>
		</tr>
		<logic:iterate id="role" name="<%= PresentationConstants.ROLES %>" scope="request">
			<tr>
				<td class="listClasses">
					<bean:define id="roleName" name="role" property="roleType"/>
					<bean:define id="roleID" name="role" property="idInternal"/>
					<html:link action="<%="/viewPersonsWithRole.do?method=searchWithRole&amp;roleID=" + roleID %>">
						<bean:message bundle="MANAGER_RESOURCES" key="<%= pageContext.findAttribute("roleName").toString() %>"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:present name="persons" scope="request">
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.name"/>
			</th>
			<th class="listClasses-header">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.username"/>
			</th>
		</tr>
		<logic:iterate id="person" name="persons" scope="request">
			<tr>
				<td class="listClasses">
					<bean:write name="person" property="name"/>
				</td>
				<td class="listClasses">
					<bean:write name="person" property="username"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
