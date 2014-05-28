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
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="${pageContext.request.contextPath}/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />

<c:forEach var="bean" items="${beans}">
	<div class="single">
	<div class="alert alert-warning">
		<strong>${academicInterval.pathName}</strong>
	</div>

	<table class="table table-bordered">
		<thead>
        	<th>
            	<bean:message key="property.room.name"/>
            </th>
			<th>
				<bean:message key="property.room.type"/>
			</th>
			<th>
				<bean:message key="property.room.building"/>
			</th>
			<th>
				<bean:message key="property.room.floor"/>
			</th>
			<th>
				<bean:message key="property.room.capacity.normal"/>
			</th>
			<th>
				<bean:message key="property.room.capacity.exame"/>
			</th>
		</thead>
		<tr>
			<td class="listClasses">
				${bean.room.name}
			</td>
			<td class="listClasses">
				${bean.room.classification.name.content}
			</td>
			<td class="listClasses">
				${bean.room.parent.name}
			</td>
			<td class="listClasses">
				x
			</td>
			<td class="listClasses">
				${bean.room.allocatableCapacity}
			</td>
			<td class="listClasses">
				y
			</td>
			
			<%-- ${bean.room.parent.metadata("level")} --%>
		</tr>
	</table>
	<c:set var="lessons" value="${bean.lessons}" />
	<div align="center"><app:gerarHorario name="lessons" type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"/></div>

	<br />
	<br />
	</div>
</c:forEach>

<style type="text/css" media="print">
.single {
	page-break-after: always;
	page-break-inside: avoid;
}
</style>

<c:if test="${beans.size() == 0}">
	<span class="error"><bean:message key="message.rooms.notExisting"/></span>
</c:if>

