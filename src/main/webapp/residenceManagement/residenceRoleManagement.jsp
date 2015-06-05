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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2> <bean:message key="title.residence.Role.Management" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2> 


<fr:form action="/residenceRoleManagement.do?method=addResidenceRoleManagemenToPerson">
	<fr:edit id="residenceRoleManagement" name="residenceRoleManagement" schema="residenceRoleManagment.searchPerson.autoComplete">
	</fr:edit>
	<p class="col-sm-offset-2">
		<html:submit>
			<bean:message key="button.residence.assign.role" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
		</html:submit>
	</p>
</fr:form>

<h3> <bean:message key="title.residence.list.roles" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h3>
<table class="table">
	<thead>
		<th>${portal.message('resources.ApplicationResources', 'label.name')}</th>
		<th>${portal.message('resources.ApplicationResources', 'label.username')}</th>
		<th></th>
	</thead>
	<tbody>
		<c:forEach items="${role.members}" var="member">
			<tr>
				<td><c:out value="${member.name}" /></td>
				<td><c:out value="${member.username}" /></td>
				<td><a href="${pageContext.request.contextPath}/residenceManagement/residenceRoleManagement.do?method=removeResidenceRoleManagemenToPerson&userToRemove=${member.username}">${portal.message('resources.ApplicationResources', 'label.remove')}</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
