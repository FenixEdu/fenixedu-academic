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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" /></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>

<div class="well" style="width: 70%">
	<bean:message bundle="MANAGER_RESOURCES" key="info.manager.findPerson"/>
</div>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
	<p>
		<span class="error0"><bean:write name="messages" /></span>
	</p>
	</html:messages>
</logic:messagesPresent>

<fr:form action="/findPerson.do?method=findPerson">
<table>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="property.login.username" />
		</td>
		<td>
			<input type="text" name="username" size="25"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.nameWord" />
		</td>
		<td>
			<input type="text" name="name" size="50"/>
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.identificationDocumentNumber" />:
		</td>
		<td>
			<input type="text" name="documentIdNumber" size="25"/>
		</td>		
	</tr>
	
	<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="label.emailWord" />
		</td>
		<td>
			<input type="text" name="email" size="25"/>
		</td>		
	</tr>
	
	<tr>
		<td>
			<br /><br />
		</td>
	</tr>	
</table>

	<html:submit bundle="HTMLALT_RESOURCES">
		<bean:message bundle="MANAGER_RESOURCES" key="button.search"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>	
</fr:form>