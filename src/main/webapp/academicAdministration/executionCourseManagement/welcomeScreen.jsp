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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%><%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message bundle="MANAGER_RESOURCES"  key="label.manager.executionCourseManagement"/></h2>
<logic:messagesPresent message="true" property="success">	<p>		<span class="success0">			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">				<bean:write name="messages" />			</html:messages>		</span>	</p></logic:messagesPresent><logic:messagesPresent message="true" property="error">	<p>		<span class="error0">			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">				<bean:write name="messages" />			</html:messages>		</span>	</p></logic:messagesPresent>
<table>
	<tr>
		<td class="infoop">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome1.intro"/><br/>					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome2.insert"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome3.edit"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome4.merge"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome5.reports"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome6.create"/>
		</td>
	</tr>
</table>
