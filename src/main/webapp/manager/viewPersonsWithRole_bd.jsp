<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
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
					<bean:define id="roleID" name="role" property="externalId"/>
					<html:link action="<%="/viewPersonsWithRole.do?method=searchWithRole&amp;roleID=" + roleID %>">
						<bean:message bundle="MANAGER_RESOURCES" key="<%= pageContext.findAttribute("roleName").toString() %>"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>


<logic:present name="persons" scope="request">
	<html:link action="<%="/viewPersonsWithRole.do?method=showRoleOperationLogs&roleID=" + request.getParameter("roleID")%>"><bean:message bundle="MANAGER_RESOURCES" key="link.show.roleoperationlog"/></html:link>
	<fr:view name="persons" schema="PersonNameAndUsername">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="sortBy" value="name"/>
			<fr:property name="linkFormat(remove)" value='<%= "/viewPersonsWithRole.do?method=removePersonFromRole&amp;personUsername=${username}&amp;roleID=" + request.getAttribute("roleID")%>' />
			<fr:property name="key(remove)" value="label.remove"/>
			<fr:property name="bundle(remove)" value="APPLICATION_RESOURCES" />
			<fr:property name="contextRelative(remove)" value="true"/>
			<fr:property name="confirmationKey(remove)" value="label.manager.delete.role.confirmation.message"/>
			<fr:property name="confirmationBundle(remove)" value="APPLICATION_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>
