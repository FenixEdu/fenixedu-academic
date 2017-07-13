<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="title.departmentForum"/></h2>

<table>
	<logic:iterate id="forum" name="foruns" type="org.fenixedu.academic.domain.messaging.DepartmentForum">
		
		<bean:size id="threadsCount" name="forum" property="conversationThreads"/>
	
		<tr>
			<td style="padding: 16px 0px 4px 0px;"><strong><bean:write name="forum" property="name"/></strong></td>
			<td></td>
		</tr>
		
		<tr>
			<td>
				<html:link action="<%=String.format("/departmentForum.do?method=viewForum&forumId=%s",forum.getExternalId())%>">
					<bean:write name="forum" property="name.content"/>
				</html:link>
			</td>
			<td>
				<span class="color888">Tópicos: <bean:write name="threadsCount"/></span>
			</td>
		</tr>
	</logic:iterate>
</table>