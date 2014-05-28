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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="section" name="component" property="section" />

	<h2><bean:write name="section" property="name" /></h2>

	<logic:notEmpty name="component" property="items">
		<logic:iterate id="item" name="component" property="items">
			<logic:equal name="item" property="urgent" value="true">
				<font color="red">
			</logic:equal>
			<h3><bean:write name="item" property="name"/></h3>
			<bean:write name="item" property="body" filter="false"/><br/>
			<logic:equal name="item" property="urgent" value="true">
				</font>
			</logic:equal>
			<logic:present name="item" property="infoFileItems">
				<br/>
				<br/>
				<table>
				<logic:iterate id="infoFileItem" name="item" property="infoFileItems">
					<bean:define id="displayName" name="infoFileItem" property="displayName" type="java.lang.String"/>
					<bean:define id="downloadUrl" name="infoFileItem" property="downloadUrl" type="java.lang.String"/>
					<tr>
						<td><img src="<%= request.getContextPath() %>/images/list-bullet.gif" alt="<bean:message key="list-bullet" bundle="IMAGE_RESOURCES" />" /></td>
						<td>
							<html:link href="<%= downloadUrl %>" ><bean:write name="infoFileItem" property="displayName"/></html:link>
						</td>
					</tr>	
				</logic:iterate>
				</table>
			</logic:present>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>