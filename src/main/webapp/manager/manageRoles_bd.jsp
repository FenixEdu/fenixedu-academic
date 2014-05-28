<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.roles"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
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
		</tr>
		<tr>
			<td>
			 	<bean:message bundle="MANAGER_RESOURCES" key="property.documentIdNumber"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.documentIdNumber" property="documentIdNumber" size="25"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" key="label.select"/>
				</html:submit>
			</td>
		</tr>
	</table>

<br />

<logic:present name="<%= PresentationConstants.ROLES %>" scope="request">
	<html:link action="/manageRoles.do?method=showRoleOperationLogs" paramId="username" paramName="person" paramProperty="istUsername"><bean:message bundle="MANAGER_RESOURCES" key="link.show.roleoperationlog"/></html:link>
	
	<table width="98%" border="0" cellpadding="0" cellspacing="0">
		<tr>
    		<td class="infoselected">
    			<strong>
	    			<bean:write name="person" property="user.username" />
   				</strong>
   				 <br/>
    			<strong><bean:write name="person" property="name"/></strong>
    			<br/>
    			<strong><bean:write name="person" property="documentIdNumber"/></strong>
			</td>
	</table>
	<br/>
	<div class="col-lg-4">
	<table class="table">
		<tr>
			<th class="listClasses-header">
			</th>
			<th class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="label.roleType"/>
			</th>
		</tr>
		<logic:iterate id="role" name="<%= PresentationConstants.ROLES %>" scope="request">
			<tr>
				<td class="listClasses">
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.roleOIDs" property="roleOIDs">
						<bean:write name="role" property="externalId"/>
					</html:multibox>
				</td>
				<td class="listClasses">
					<bean:define id="roleName" name="role" property="roleType"/>
					<bean:message bundle="MANAGER_RESOURCES" key="<%= pageContext.findAttribute("roleName").toString() %>"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
	</div>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='setPersonRoles';this.form.page.value=2;">
		<bean:message bundle="MANAGER_RESOURCES" key="label.setRoles"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</logic:present>

</html:form>