<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.roles"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<html:form action="/manageRoles" focus="username">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectUser"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table>
		<tr>
			<td>
			 	<bean:message bundle="MANAGER_RESOURCES" key="property.login.username"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" size="25"/>
			</td>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" key="label.select"/>
				</html:submit>
			</td>
		</tr>
	</table>

<br />

<logic:present name="<%= SessionConstants.ROLES %>" scope="request">

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
    		<td class="infoselected">
    			Utilizador selecionado:
    			<strong><bean:write name="username"/></strong>
			</td>
		</tr>
	</table>

	<table>
		<tr>
			<th class="listClasses-header">
			</th>
			<th class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="label.roleType"/>
			</th>
		</tr>
		<logic:iterate id="role" name="<%= SessionConstants.ROLES %>" scope="request">
			<tr>
				<td class="listClasses">
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.roleOIDs" property="roleOIDs">
						<bean:write name="role" property="idInternal"/>
					</html:multibox>
				</td>
				<td class="listClasses">
					<bean:define id="roleName" name="role" property="roleType"/>
					<bean:message bundle="MANAGER_RESOURCES" key="<%= pageContext.findAttribute("roleName").toString() %>"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='setPersonRoles';this.form.page.value=2;">
		<bean:message bundle="MANAGER_RESOURCES" key="label.setRoles"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</logic:present>

</html:form>