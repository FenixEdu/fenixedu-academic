<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.manage.roles"/></h2>
<br />
<span class="error"><html:errors/></span>

<html:form action="/manageRoles" focus="username">
	<html:hidden property="method" value="selectUser"/>
	<html:hidden property="page" value="1"/>

	<table>
		<tr>
			<td>
			 	<bean:message key="property.login.username"/>
			</td>
			<td>
				<html:text property="username" size="25"/>
			</td>
			<td>
				<html:submit styleClass="inputbutton">
					<bean:message key="label.select"/>
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
			<td class="listClasses-header">
			</td>
			<td class="listClasses-header">
				<bean:message key="label.roleType"/>
			</td>
		</tr>
		<logic:iterate id="role" name="<%= SessionConstants.ROLES %>" scope="request">
			<tr>
				<td class="listClasses">
					<html:multibox property="roleOIDs">
						<bean:write name="role" property="idInternal"/>
					</html:multibox>
				</td>
				<td class="listClasses">
					<bean:define id="roleName" name="role" property="roleType.name"/>
					<bean:message key="<%= pageContext.findAttribute("roleName").toString() %>"/>
				</td>			
			</tr>
		</logic:iterate>
	</table>
	<br />
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='setPersonRoles';this.form.page.value=2;">
		<bean:message key="label.setRoles"/>
	</html:submit>
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</logic:present>

</html:form>